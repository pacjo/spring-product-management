package nodomain.cloudfide.task.mappers;

import nodomain.cloudfide.task.dto.request.CreateProducerRequest;
import nodomain.cloudfide.task.dto.request.UpdateProducerRequest;
import nodomain.cloudfide.task.dto.response.ProducerResponse;
import nodomain.cloudfide.task.entity.Producer;
import org.springframework.stereotype.Component;

@Component
public class ProducerMapper {

    public Producer toEntity(CreateProducerRequest request) {
        Producer producer = new Producer();
        producer.setName(request.getName());
        producer.setDescription(request.getDescription());
        return producer;
    }

    public void updateEntity(Producer producer, UpdateProducerRequest request) {
        if (request.getName() != null)
            producer.setName(request.getName());

        if (request.getDescription() != null)
            producer.setDescription(request.getDescription());
    }

    public ProducerResponse toResponse(Producer producer) {
        return new ProducerResponse(
            producer.getId(),
            producer.getName(),
            producer.getDescription(),
            producer.getCreatedAt(),
            producer.getUpdatedAt()
        );
    }
}