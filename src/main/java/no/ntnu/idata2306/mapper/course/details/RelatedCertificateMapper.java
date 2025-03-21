package no.ntnu.idata2306.mapper.course.details;

import no.ntnu.idata2306.dto.course.details.RelatedCertificateDto;
import no.ntnu.idata2306.model.course.details.RelatedCertificate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RelatedCertificateMapper {
    RelatedCertificateMapper INSTANCE = Mappers.getMapper(RelatedCertificateMapper.class);

    RelatedCertificateDto relatedCertificateToRelatedCertificateDto(RelatedCertificate relatedCertificate);
}
