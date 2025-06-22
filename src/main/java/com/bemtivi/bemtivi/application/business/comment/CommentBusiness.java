package com.bemtivi.bemtivi.application.business.comment;

import com.bemtivi.bemtivi.application.domain.ActivationStatus;
import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.comment.Comment;
import com.bemtivi.bemtivi.application.enums.TypeComment;
import com.bemtivi.bemtivi.exceptions.DatabaseIntegrityViolationException;
import com.bemtivi.bemtivi.exceptions.ResourceNotFoundException;
import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import com.bemtivi.bemtivi.persistence.entities.comment.CommentEntity;
import com.bemtivi.bemtivi.persistence.entities.product.ProductEntity;
import com.bemtivi.bemtivi.persistence.entities.service.ServiceEntity;
import com.bemtivi.bemtivi.persistence.mappers.CommentPersistenceMapper;
import com.bemtivi.bemtivi.persistence.repositories.CommentRepository;
import com.bemtivi.bemtivi.persistence.repositories.CustomerRepository;
import com.bemtivi.bemtivi.persistence.repositories.ProductRepository;
import com.bemtivi.bemtivi.persistence.repositories.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentBusiness {
    private final ServiceRepository serviceRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CommentRepository commentRepository;
    private final CommentPersistenceMapper mapper;

    public PageResponse<Comment> findByServiceId(Boolean isActive, Integer pageSize, Integer page, String serviceId) {
        PageResponse<Comment> pageResponse= mapper.mapToPageResponseDomain(
                commentRepository.findByActivationStatus_IsActiveAndService_Id(isActive, serviceId, PageRequest.of(page, pageSize)
        ));
        pageResponse.setTotalRate(getTotalRate(commentRepository.findByActivationStatus_IsActiveAndService_Id(isActive, serviceId)));
        return pageResponse;
    }

    public PageResponse<Comment> findByProductId(Boolean isActive, Integer pageSize, Integer page, String productId) {
        PageResponse<Comment> pageResponse= mapper.mapToPageResponseDomain(
                commentRepository.findByActivationStatus_IsActiveAndProduct_Id(isActive, productId, PageRequest.of(page, pageSize)
        ));
        pageResponse.setTotalRate(getTotalRate(commentRepository.findByActivationStatus_IsActiveAndProduct_Id(isActive, productId)));
        return pageResponse;
    }

    public Comment insert(Comment comment) {
        CommentEntity saved;
        ActivationStatus activationStatus = ActivationStatus.builder()
                .isActive(true)
                .creationDate(Instant.now())
                .build();

        if (comment.getTypeComment().equals(TypeComment.SERVICE)) {
            ServiceEntity service = serviceRepository.findById(comment.getService().getId()).orElseThrow(
                    () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0005)
            );
            updateRateService(service);
        } else if (comment.getTypeComment().equals(TypeComment.PRODUCT)) {
            ProductEntity product = productRepository.findById(comment.getProduct().getId()).orElseThrow(
                    () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0003)
            );
            updateRateProduct(product);
        }

        customerRepository.findById(comment.getCustomer().getId()).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );

        try {
            comment.setId(null);
            comment.setActivationStatus(activationStatus);
            CommentEntity commentEntity = mapper.mapToEntity(comment);
            saved = commentRepository.save(commentEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(saved);
    }

    public Comment update(String id, Comment commentNew) {
        CommentEntity commentOld = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0006)
        );
        commentOld.setTitle(commentNew.getTitle() == null ? commentOld.getTitle() : commentNew.getTitle());
        commentOld.setRate(commentNew.getRate() == null ? commentOld.getRate() : commentNew.getRate());
        commentOld.setMessage(commentNew.getMessage() == null ? commentOld.getMessage() : commentNew.getMessage());

        CommentEntity updated;
        try {
            updated = commentRepository.save(commentOld);
        } catch (DataIntegrityViolationException exception) {
            throw new DatabaseIntegrityViolationException(RuntimeErrorEnum.ERR0002);
        }
        return mapper.mapToDomain(updated);
    }

    public void deactivate(String id) {
        CommentEntity comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0011)
        );
        comment.getActivationStatus().setIsActive(false);
        comment.getActivationStatus().setDeactivationDate(Instant.now());
        commentRepository.save(comment);
    }

    public void delete(String id) {
        CommentEntity comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(RuntimeErrorEnum.ERR0011)
        );
        commentRepository.delete(comment);
    }

    private void updateRateService(ServiceEntity service) {
        service.setRate(getTotalRate(
                commentRepository.findByActivationStatus_IsActiveAndService_Id(true, service.getId())
        ));
        serviceRepository.save(service);
    }

    private void updateRateProduct(ProductEntity product) {
        product.setRate(getTotalRate(
                commentRepository.findByActivationStatus_IsActiveAndProduct_Id(true, product.getId())
        ));
        productRepository.save(product);
    }

    private double getTotalRate(List<CommentEntity> commentEntityList) {
        if (commentEntityList.isEmpty()) {
            return 0d;
        }

        double totalRate = 0d;
        for (CommentEntity comment : commentEntityList) {
            totalRate += comment.getRate();
        }
        return totalRate / commentEntityList.size();
    }
}
