package com.bemtivi.bemtivi.application.business.user;

import com.bemtivi.bemtivi.application.business.EmailBusiness;
import com.bemtivi.bemtivi.application.business.UploadBusiness;
import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.email.Email;
import com.bemtivi.bemtivi.application.domain.user.customer.Address;
import com.bemtivi.bemtivi.application.domain.user.customer.Customer;
import com.bemtivi.bemtivi.application.domain.user.customer.Telephone;
import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.bemtivi.bemtivi.controllers.auth.dto.UserAuthDTO;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.PasswordsDTO;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.DuplicateResourceException;
import com.bemtivi.bemtivi.exceptions.OperationNotAllowedException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.administrator.AdministratorEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.AddressEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.entities.customer.TelephoneEntity;
import com.bemtivi.bemtivi.persistence.mappers.CustomerPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.AdministratorRepository;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomerBusiness {
    private final CustomerRepository customerRepository;
    private final AdministratorRepository administratorRepository;
    private final CustomerPersistenceMapper mapper;
    private final UploadBusiness uploadManager;
    private final EmailBusiness emailBusiness;

    public PageResponse<Customer> paginate(Boolean isActive, Integer pageSize, Integer page, String name) {
        return mapper.mapToPageResponseDomain(
                customerRepository.findByActivationStatus_IsActiveAndNameContainingIgnoreCase(isActive, name == null ? "" : name, PageRequest.of(page, pageSize))
        );
    }

    public Customer findById(String id) {
        return mapper.mapToDomain(checkIfTheIdIsValidAndReturnCustomer(id));
    }

    public Customer insert(Customer customer, MultipartFile file) {
        checkIfTheEmailAlreadyExists(customer.getEmail());

        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        customer.setId(null);
        customer.setActivationStatus(activationStatus);
        customer.setPathImage(uploadManager.uploadObject(file));
        customer.setRole(UserRoleEnum.CUSTOMER);
        customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
        customer.setIsEmailActive(false);
        CustomerEntity saved;
        try {
            saved = customerRepository.save(mapper.mapToEntity(customer));
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public Customer update(String id, Customer customerNew, MultipartFile file) {
        CustomerEntity customerOld = checkIfTheIdIsValidAndReturnCustomer(id);

        customerOld.setName(customerNew.getName() == null ? customerOld.getName() : customerNew.getName());
        customerOld.setBirthDate(customerNew.getBirthDate() == null ? customerOld.getBirthDate() : customerNew.getBirthDate());

        if (customerNew.getTelephones() != null) {
            Telephone telephoneNew = customerNew.getTelephones();
            TelephoneEntity telephoneOld = customerOld.getTelephones();
            customerOld.setTelephones(TelephoneEntity.builder()
                    .id(telephoneOld.getId())
                    .phoneOne(telephoneNew.getPhoneOne() == null ? telephoneOld.getPhoneOne() : telephoneNew.getPhoneOne())
                    .phoneTwo(telephoneNew.getPhoneTwo() == null ? telephoneOld.getPhoneTwo() : telephoneNew.getPhoneTwo())
                    .build());
        }

        if (customerNew.getAddress() != null) {
            Address addressNew = customerNew.getAddress();
            AddressEntity addressOld = customerOld.getAddress();
            customerOld.setAddress(AddressEntity.builder()
                            .id(addressOld.getId())
                            .state(addressNew.getState() == null ? addressOld.getState() : addressNew.getState())
                            .city(addressNew.getCity() == null ? addressOld.getCity() : addressNew.getCity())
                            .neighborhood(addressNew.getNeighborhood() == null ? addressOld.getNeighborhood() : addressNew.getNeighborhood())
                            .street(addressNew.getStreet() == null ? addressOld.getStreet() : addressNew.getStreet())
                            .number(addressNew.getNumber() == null ? addressOld.getNumber() : addressNew.getNumber())
                            .complement(addressNew.getComplement() == null ? addressOld.getComplement() : addressNew.getComplement())
                            .postalCode(addressNew.getPostalCode() == null ? addressOld.getPostalCode() : addressNew.getPostalCode())
                            .build());
        }
        if (file != null) customerOld.setPathImage(uploadManager.uploadObject(file));
        CustomerEntity updated;
        try {
            updated = customerRepository.save(customerOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void updatePassword(String id, PasswordsDTO passwords) {
        CustomerEntity customer = checkIfTheIdIsValidAndReturnCustomer(id);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(passwords.passwordOld(), customer.getPassword())) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0019);
        }

        if (passwords.passwordNew() != null && !passwords.passwordNew().isEmpty()) {
            String newPasswordEncrypted = encoder.encode(passwords.passwordNew());
            customer.setPassword(newPasswordEncrypted);
        }

        try {
            customerRepository.save(customer);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
    }

    public void sendRequestRetrievePassword(String emailAddress) {
        CustomerEntity customer = checkIfTheEmailIsValidAndReturnCustomer(emailAddress);
        String code = generateRandomCode();

        Email email = new Email();
        email.setTo(emailAddress);
        email.setSubject("Recuperação de senha");
        email.setContent(
                "Olá,\n\n" +
                        "Recebemos uma solicitação para recuperação da senha da sua conta.\n\n" +
                        "Para continuar com o processo de redefinição, utilize o seguinte código de verificação:\n\n" +
                        "Código: " + code + "\n\n" +
                        "Se você não fez essa solicitação, por favor ignore este e-mail ou entre em contato com o nosso suporte.\n\n" +
                        "Atenciosamente,\nEquipe Mister Gold"
        );
        emailBusiness.sendEmail(email);

        customer.setCodeForPassword(code + "&" + emailAddress);
        customerRepository.save(customer);
    }

    public void retrievePassword(String id, String code) {
        CustomerEntity customer = checkIfTheIdIsValidAndReturnCustomer(id);
        String codeSaved = customer.getCodeForPassword().split("&")[0];
        String password = customer.getCodeForPassword().split("&")[1];

        customer.setCodeForPassword(null);
        if (codeSaved.equals(code)) {
            customer.setPassword(new BCryptPasswordEncoder().encode(password));
            customerRepository.save(customer);
        } else {
            customerRepository.save(customer);
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0020);
        }
    }

    public void sendRequestEmail(String id, String newEmail) {
        CustomerEntity customer = checkIfTheIdIsValidAndReturnCustomer(id);
        checkIfTheEmailAlreadyExists(newEmail);
        String code = generateRandomCode();

        Email email = new Email();
        email.setTo(newEmail);
        email.setSubject("Confirmação de alteração de e-mail");
        email.setContent(
                "Olá,\n\n" +
                "Recebemos uma solicitação para alterar o e-mail da sua conta.\n\n" +
                "Para confirmar essa alteração, utilize o seguinte código de verificação:\n\n" +
                "Código: " + code + "\n\n" +
                "Se você não fez essa solicitação, por favor ignore este e-mail ou entre em contato com o nosso suporte.\n\n" +
                "Atenciosamente,\nEquipe Mister Gold"
        );
        emailBusiness.sendEmail(email);

        customer.setCodeForEmail(code + "&" + newEmail);
        customerRepository.save(customer);
    }

    public void updateEmail(String id, String code, String newEmail) {
        CustomerEntity customer = checkIfTheIdIsValidAndReturnCustomer(id);
        String codeSaved = customer.getCodeForEmail().split("&")[0];
        String emailSaved = customer.getCodeForEmail().split("&")[1];
        checkIfTheEmailAlreadyExists(emailSaved);

        customer.setCodeForEmail(null);
        if (codeSaved.equals(code)) {
            customer.setEmail(newEmail);
            customerRepository.save(customer);
        } else {
            customerRepository.save(customer);
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0020);
        }
    }

    public void sendRequestConfirmationEmail(String id, String newEmail) {
        CustomerEntity customer = checkIfTheIdIsValidAndReturnCustomer(id);
        checkIfTheEmailAlreadyExists(newEmail);
        String code = generateRandomCode();

        Email email = new Email();
        email.setTo(newEmail);
        email.setSubject("Confirmação de e-mail");
        email.setContent(
                "Olá,\n\n" +
                        "Recebemos uma solicitação para confirmar este endereço de e-mail em sua conta Mister Gold.\n\n" +
                        "Para concluir o processo de confirmação, utilize o código de verificação abaixo:\n\n" +
                        "Código de verificação: " + code + "\n\n" +
                        "Se você não fez essa solicitação, por favor ignore este e-mail ou entre em contato com o nosso suporte.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe Mister Gold"
        );
        emailBusiness.sendEmail(email);

        customer.setCodeForEmail(code + "&" + newEmail);
        customerRepository.save(customer);
    }

    public void confirmationEmail(String id, String code) {
        CustomerEntity customer = checkIfTheIdIsValidAndReturnCustomer(id);
        String codeSaved = customer.getCodeForEmail().split("&")[0];
        String emailSaved = customer.getCodeForEmail().split("&")[1];
        checkIfTheEmailAlreadyExists(emailSaved);

        customer.setCodeForEmail(null);
        if (codeSaved.equals(code)) {
            customer.setEmail(emailSaved);
            customer.setIsEmailActive(true);
            customerRepository.save(customer);
        } else {
            customerRepository.save(customer);
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0020);
        }
    }

    public void deactivate(String id) {
        CustomerEntity customer = checkIfTheIdIsValidAndReturnCustomer(id);



        customer.getActivationStatus().setIsActive(false);
        customer.getActivationStatus().setDeactivationDate(Instant.now());
        customerRepository.save(customer);
    }

    public void delete(String id, String password) {
        CustomerEntity customer = checkIfTheIdIsValidAndReturnCustomer(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(password, customer.getPassword())) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0019);
        }

        customerRepository.delete(customer);
    }

    private CustomerEntity checkIfTheIdIsValidAndReturnCustomer(String id) {
        return customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );
    }

    private CustomerEntity checkIfTheEmailIsValidAndReturnCustomer(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );
    }

    private void checkIfTheEmailAlreadyExists(String email) {
        customerRepository.findByUsername(email).ifPresent((register) -> {
            throw new DuplicateResourceException(RuntimeErrorEnum.ERR0013);
        });

        administratorRepository.findByUsername(email).ifPresent((register) -> {
            throw new DuplicateResourceException(RuntimeErrorEnum.ERR0013);
        });
    }

    private String generateRandomCode() {
        Random random = new Random();

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}
