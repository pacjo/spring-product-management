package nodomain.cloudfide.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducerPageResponse {
    private List<ProducerResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}