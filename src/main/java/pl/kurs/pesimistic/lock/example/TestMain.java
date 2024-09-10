package pl.kurs.pesimistic.lock.example;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestTemplate;
import pl.kurs.pesimistic.lock.example.model.command.CreateVisitCommand;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestMain {

    public static void main(String[] args) {

        LocalDateTime startDate = LocalDateTime.of(2020, Month.JANUARY, 2, 20, 0, 0, 0);
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 5; i++) {
            CreateVisitCommand command = new CreateVisitCommand();
            command.setDate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(startDate));
            command.setDoctorId(1);

            System.out.println("Test iteration: " + (i + 1));
            List<Future<HttpStatus>> futures = new ArrayList<>(10);
            List<HttpStatus> statuses = new ArrayList<>(10);

            for (int j = 0; j < 10; j++) {
                futures.add(executorService.submit(new RequestCall(command)));
            }

            for (int j = 0; j < 10; j++) {
                try {
                    statuses.add(futures.get(j).get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (statuses.stream().filter(s -> s.is2xxSuccessful()).count() != 1) {
                System.out.println("\tERROR!! [" + i + ", " + statuses);
                break;
            }

            startDate = startDate.plusDays(1);
        }
        executorService.shutdown();
    }

    @RequiredArgsConstructor
    static class RequestCall implements Callable<HttpStatus> {
        private final CreateVisitCommand command;

        @Override
        public HttpStatus call() {
            RestTemplate restTemplate = new RestTemplate();
            try {
                HttpStatusCode httpStatusCode = restTemplate.postForEntity("http://localhost:8080/visits", command, Void.class).getStatusCode();
                return HttpStatus.valueOf(httpStatusCode.value());
            } catch (Exception exc) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}
