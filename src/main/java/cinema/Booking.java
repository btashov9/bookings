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

    public static final int NUM_SEATS = 50;
    public static final int NUM_ROWS = 100;
    public static final int OVER_LIMIT_SEATS = 6;

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
            for (String line : Files.readAllLines(path)) {

                List<Integer> bookingLine = Arrays.stream(line.replaceAll("\\(", "").split("\\W"))
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());

                Request req = Request.builder()
                        .id(bookingLine.get(0))
                        .firstRow(bookingLine.get(1))
                        .firstPlace(bookingLine.get(2))
                        .lastRow(bookingLine.get(3))
                        .lastPlace(bookingLine.get(4))
                        .isOk(true)
                        .build();

                if (isPlaceInCinemaRange(req) || isAnotherRow(req) || isOverLimit(req)) {
                    req.setOk(false);
                } else {
                    if (isOccupiedPlace(req)) {
                        req.setOk(false);
                    }
                    provideSeat(req);
                }
                requests.add(req);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return requests;

    }

    private boolean isOverLimit(Request req) {
        return (req.getLastPlace() - req.getFirstPlace()+1) >= OVER_LIMIT_SEATS;
    }

    private boolean isAnotherRow(Request req) {
        return req.getFirstRow() != req.getLastRow();
    }

    private boolean isPlaceInCinemaRange(Request req) {
        return req.getFirstRow() > NUM_ROWS
                || req.getFirstPlace() > NUM_SEATS
                || req.getLastRow() > NUM_ROWS
                || req.getLastPlace() > NUM_SEATS;
    }

    private void provideSeat(Request req) {
        if (req.isOk()) {
            int size = req.getLastPlace() - req.getFirstPlace();
            for (int i = 0; i <= size; i++) {
                CINEMA[req.getFirstRow()][req.getFirstPlace() + i] = true;  //todo check
            }
        }
    }

    private boolean isOccupiedPlace(Request req) {
        return CINEMA[req.getFirstRow()][req.getFirstPlace()] || CINEMA[req.getLastRow()][req.getLastPlace()];
    }



}