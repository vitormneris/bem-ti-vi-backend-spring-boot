package com.bemtivi.bemtivi.application.business.user;

import com.bemtivi.bemtivi.application.business.EmailBusiness;
import com.bemtivi.bemtivi.application.business.UploadBusiness;
import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.email.Email;
import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.application.enums.UserRoleEnum;
import com.bemtivi.bemtivi.controllers.in.administrator.dto.PasswordsDTO;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.OperationNotAllowedException;
import com.bemtivi.bemtivi.persistence.entities.customer.CustomerEntity;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.DuplicateResourceException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.administrator.AdministratorEntity;
import com.bemtivi.bemtivi.persistence.mappers.AdministratorPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdministratorBusiness {
    private final AdministratorRepository administratorRepository;
    private final CustomerRepository customerRepository;
    private final AdministratorPersistenceMapper mapper;
    private final UploadBusiness uploadBusiness;
    private final EmailBusiness emailBusiness;

    public Set<Administrator> findByActivationStatus(Boolean active) {
        return mapper.mapToSetDomain(new LinkedHashSet<>(administratorRepository.findByActivationStatus_IsActive(active)));
    }

    public Administrator findById(String id) {
        return mapper.mapToDomain(checkIfTheIdIsValidAndReturnAdministrator(id));
    }

    public Administrator insert(Administrator administrator, MultipartFile file) {
        checkIfTheEmailAlreadyExists(administrator.getEmail());

        if (administratorRepository.findAll().size() >= 5) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0017);
        }

        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();
        administrator.setId(null);
        administrator.setRole(UserRoleEnum.ADMINISTRATOR);
        administrator.setPassword(new BCryptPasswordEncoder().encode(administrator.getPassword()));
        administrator.setActivationStatus(activationStatus);
        administrator.setPathImage(uploadBusiness.uploadObject(file));
        administrator.setIsEmailActive(false);
        AdministratorEntity saved;
        try {
            saved = administratorRepository.save(mapper.mapToEntity(administrator));
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException((RuntimeErrorEnum.ERR0002));
        }
        return mapper.mapToDomain(saved);
    }

    public Administrator update(String id, Administrator administratorNew, MultipartFile file) {
        AdministratorEntity administratorOld = checkIfTheIdIsValidAndReturnAdministrator(id);

        administratorOld.setName(administratorNew.getName() == null ? administratorOld.getName() : administratorNew.getName());
        if (file != null) administratorOld.setPathImage(uploadBusiness.uploadObject(file));
        AdministratorEntity updated;
        try {
            updated = administratorRepository.save(administratorOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void updatePassword(String id, PasswordsDTO passwords) {
        AdministratorEntity administrator = checkIfTheIdIsValidAndReturnAdministrator(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(passwords.passwordOld(), administrator.getPassword())) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0019);
        }

        if (passwords.passwordNew() != null && !passwords.passwordNew().isEmpty()) {
            String newPasswordEncrypted = encoder.encode(passwords.passwordNew());
            administrator.setPassword(newPasswordEncrypted);
        }

        try {
            administratorRepository.save(administrator);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
    }


    public void sendRequestEmail(String id, String newEmail) {
        AdministratorEntity administrator = checkIfTheIdIsValidAndReturnAdministrator(id);
        checkIfTheEmailAlreadyExists(newEmail);
        String code = generateRandomCode();

        Email email = new Email();
        email.setTo(newEmail);
        email.setSubject("Confirmação de alteração de e-mail");
        email.setContent("Olá,\n\n" +
                "Recebemos uma solicitação para alterar o e-mail da sua conta.\n\n" +
                "Para confirmar essa alteração, utilize o seguinte código de verificação:\n\n" +
                "Código: " + code + "\n\n" +
                "Se você não fez essa solicitação, por favor ignore este e-mail ou entre em contato com o nosso suporte.\n\n" +
                "Atenciosamente,\nEquipe Mister Gold");
        emailBusiness.sendEmail(email);

        administrator.setCode(code + "&" + newEmail);
        administratorRepository.save(administrator);
    }

    public void updateEmail(String id, String code) {
        AdministratorEntity administrator = checkIfTheIdIsValidAndReturnAdministrator(id);
        String codeSaved = administrator.getCode().split("&")[0];
        String emailSaved = administrator.getCode().split("&")[1];
        checkIfTheEmailAlreadyExists(emailSaved);

        administrator.setCode(null);
        if (codeSaved.equals(code)) {
            administrator.setEmail(emailSaved);
            administratorRepository.save(administrator);
        } else {
            administratorRepository.save(administrator);
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0020);
        }
    }

    public void sendRequestConfirmationEmail(String id, String newEmail) {
        AdministratorEntity administrator = checkIfTheIdIsValidAndReturnAdministrator(id);
        if (!administrator.getEmail().equals(newEmail)) {
            checkIfTheEmailAlreadyExists(newEmail);
        }
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

        administrator.setCode(code + "&" + newEmail);
        administratorRepository.save(administrator);
    }

    public void confirmationEmail(String id, String code) {
        AdministratorEntity administrator = checkIfTheIdIsValidAndReturnAdministrator(id);
        String codeSaved = administrator.getCode().split("&")[0];
        String emailSaved = administrator.getCode().split("&")[1];
        if (!administrator.getEmail().equals(emailSaved)) {
            checkIfTheEmailAlreadyExists(emailSaved);
        }
        administrator.setCode(null);
        if (codeSaved.equals(code)) {
            administrator.setEmail(emailSaved);
            administrator.setIsEmailActive(true);
            administratorRepository.save(administrator);
        } else {
            administratorRepository.save(administrator);
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0020);
        }
    }

    public void deactivate(String id) {
        AdministratorEntity administrator = checkIfTheIdIsValidAndReturnAdministrator(id);

        if (administratorRepository.findAll().size() <= 1) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0022);
        }

        administrator.getActivationStatus().setIsActive(false);
        administrator.getActivationStatus().setDeactivationDate(Instant.now());
        administratorRepository.save(administrator);
    }

    public void activate(String id) {
        AdministratorEntity administrator = checkIfTheIdIsValidAndReturnAdministrator(id);
        administrator.getActivationStatus().setIsActive(true);
        administrator.getActivationStatus().setDeactivationDate(null);
        administratorRepository.save(administrator);
    }

    public void delete(String id, String password) {
        AdministratorEntity administrator = checkIfTheIdIsValidAndReturnAdministrator(id);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (administratorRepository.findAll().size() <= 1) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0022);
        }

        if (!encoder.matches(password, administrator.getPassword())) {
            throw new OperationNotAllowedException(RuntimeErrorEnum.ERR0019);
        }

        administratorRepository.delete(administrator);
    }

    private AdministratorEntity checkIfTheIdIsValidAndReturnAdministrator(String id) {
        return administratorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0010)
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
