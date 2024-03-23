package voting.app.voting.app.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import voting.app.voting.app.config.AppConfig;

@Component
@RequiredArgsConstructor
public class PaginationHelper {
    private final AppConfig appConfig;

    public Pageable getPageable(
            Integer pageNumber, Integer pageSize, boolean isAscending, String... sortBy) {
        if (pageSize == null) {
            pageSize = appConfig.getPaginationConfig().getDefaultPageSize();
        }

        if (pageSize > appConfig.getPaginationConfig().getMaxPageSize()) {
            pageSize = appConfig.getPaginationConfig().getMaxPageSize();
        }

        return PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(isAscending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy));
    }
}
