package cinema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class Booking {

    public static final int MAX_SEATS = 5;
    public static final int NUM_SEATS = 50;
    public static final int NUM_ROWS = 100;

    public static boolean[][] CINEMA = new boolean[NUM_ROWS][NUM_SEATS];

    public static void main(String args[]) {

        new Booking().processBookings()
                .stream()
                .filter(x -> !x.isOk())
                .forEach(e -> System.out.println("Booking request id: "+e.getId()));

    }

    private List<Request> processBookings() {

        Path path = Paths.get("src/main/resources/sample_booking_requests");
        List<Request> requests = new LinkedList<>();
        try {

            List<String> contents = Files.readAllLines(path);
            for (String ticket : contents) {
                String[] bookingArray = ticket.replaceAll("\\(", "").split("\\W");

                List<Integer> bookingList = Arrays.stream(bookingArray)
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());


                Request req = Request.builder()
                        .id(bookingList.get(0))
                        .firstRow(bookingList.get(1))
                        .firstPlace(bookingList.get(2))
                        .lastRow(bookingList.get(3))
                        .lastPlace(bookingList.get(4))
                        .isOk(true)
                        .build();

                if (req.getFirstRow() > NUM_ROWS
                        || req.getFirstPlace() > NUM_SEATS
                        || req.getLastRow() > NUM_ROWS
                        || req.getLastPlace() > NUM_SEATS) {
                    req.setOk(false);
                } else {
                    if (req.getFirstRow() != req.getLastRow()) {
                        req.setOk(false);
                    } else {
                        if ((req.getLastPlace() - req.getFirstPlace()) > MAX_SEATS) {
                            req.setOk(false);
                        } else {
                            if (CINEMA[req.getFirstRow()][req.getFirstPlace()]
                                    || CINEMA[req.getLastRow()][req.getLastPlace()]
                            ) {
                                req.setOk(false);
                            }

                            if (req.isOk()) {
                                int size = req.getLastPlace() - req.getFirstPlace();
                                for (int i = 0; i <= size; i++) {
                                    CINEMA[req.getFirstRow()][req.getFirstPlace() + i] = true;
                                }
                            }
                        }
                    }
                }


                requests.add(req);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return requests;

    }

}