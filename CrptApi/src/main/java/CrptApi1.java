import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.Semaphore;

public class CrptApi1 {
    private static final String API_URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private final Semaphore semaphore;
    private final long intervalMillis;

    public CrptApi1(long intervalMillis, int requestLimit) {
        this.intervalMillis = intervalMillis;
        this.semaphore = new Semaphore(requestLimit);
        startRateLimiter(requestLimit);
    }

    private void startRateLimiter(int requestLimit) {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(intervalMillis);
                    semaphore.release(requestLimit - semaphore.availablePermits());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void createDocument(Document document, String signature) throws Exception {
        semaphore.acquire();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String jsonDocument = mapper.writeValueAsString(document);

        // Simulate API call by printing the request details
        System.out.println("Simulating API call...");
        System.out.println("POST " + API_URL);
        System.out.println("Headers: Content-Type: application/json, Signature: " + signature);
        System.out.println("Body: " + jsonDocument);

        // Simulated response
        System.out.println("Document created successfully (simulated).");
    }

    // Internal classes representing the document structure
    public static class Document {
        public Description description;
        public String doc_id;
        public String doc_status;
        public String doc_type;
        public boolean importRequest;
        public String owner_inn;
        public String participant_inn;
        public String producer_inn;
        public String production_date;
        public String production_type;
        public Product[] products;
        public String reg_date;
        public String reg_number;
    }

    public static class Description {
        public String participantInn;
    }

    public static class Product {
        public String certificate_document;
        public String certificate_document_date;
        public String certificate_document_number;
        public String owner_inn;
        public String producer_inn;
        public String production_date;
        public String tnved_code;
        public String uit_code;
        public String uitu_code;
    }

    public static void main(String[] args) {
        try {
            CrptApi1 api = new CrptApi1(60000, 10);  // Example: 10 requests per minute

            Description description = new Description();
            description.participantInn = "string";

            Product product = new Product();
            product.certificate_document = "string";
            product.certificate_document_date = "2020-01-23";
            product.certificate_document_number = "string";
            product.owner_inn = "string";
            product.producer_inn = "string";
            product.production_date = "2020-01-23";
            product.tnved_code = "string";
            product.uit_code = "string";
            product.uitu_code = "string";

            Document document = new Document();
            document.description = description;
            document.doc_id = "string";
            document.doc_status = "string";
            document.doc_type = "LP_INTRODUCE_GOODS";
            document.importRequest = true;
            document.owner_inn = "string";
            document.participant_inn = "string";
            document.producer_inn = "string";
            document.production_date = "2020-01-23";
            document.production_type = "string";
            document.products = new Product[]{product};
            document.reg_date = "2020-01-23";
            document.reg_number = "string";

            api.createDocument(document, "signature");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
