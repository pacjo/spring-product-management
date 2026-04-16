package nodomain.cloudfide.task.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {

    public static Pageable createPageable(int page, int size, String sort) {
        if (sort != null && !sort.isBlank()) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                Sort.Direction direction = sortParams[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;
                return PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
            }
        }
        return PageRequest.of(page, size);
    }
}