package cinema;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
class Request {
    private final int id;
    private final int firstRow;
    private final int firstPlace;
    private final int lastRow;
    private final int lastPlace;
    private boolean isOk;

}
