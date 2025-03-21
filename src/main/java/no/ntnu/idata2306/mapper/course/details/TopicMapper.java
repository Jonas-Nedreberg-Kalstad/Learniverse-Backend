package no.ntnu.idata2306.mapper.course.details;

import no.ntnu.idata2306.dto.course.details.TopicDto;
import no.ntnu.idata2306.model.course.details.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    TopicDto topicToTopicDto(Topic topic);
}
