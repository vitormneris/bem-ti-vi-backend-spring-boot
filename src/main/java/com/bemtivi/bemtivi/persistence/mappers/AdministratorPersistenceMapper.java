package com.bemtivi.bemtivi.persistence.mappers;

import com.bemtivi.bemtivi.application.domain.PageResponse;
import com.bemtivi.bemtivi.application.domain.user.administrator.Administrator;
import com.bemtivi.bemtivi.persistence.entities.administrator.AdministratorEntity;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AdministratorPersistenceMapper {
    AdministratorEntity mapToEntity(Administrator Administrator);
    Administrator mapToDomain(AdministratorEntity AdministratorEntity);

    default PageResponse<Administrator> mapToPageResponseDomain(Page<AdministratorEntity> pageResponse) {
        int previousPage = pageResponse.hasPrevious() ? pageResponse.getNumber() - 1 : pageResponse.getNumber();
        int nextPage = pageResponse.hasNext() ? pageResponse.getNumber() + 1 : pageResponse.getNumber();
        Set<Administrator> administrators = pageResponse.getContent().stream().map(this::mapToDomain).collect(Collectors.toSet());
        return PageResponse.<Administrator>builder()
                .pageSize(pageResponse.getNumberOfElements())
                .totalElements(pageResponse.getTotalElements())
                .currentPage(pageResponse.getNumber())
                .previousPage(previousPage)
                .nextPage(nextPage)
                .content(administrators)
                .totalPages(pageResponse.getTotalPages())
                .build();
    }

}
