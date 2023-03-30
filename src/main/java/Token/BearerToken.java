package Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.*;

public class BearerToken {
    private List<JSONObject> tests = new ArrayList<>();
    public void changeResult(String testCaseName, Boolean isPassed) {
        JSONObject test = new JSONObject();
        test.put("testKey", testCaseName);
        test.put("start", "2023-09-03T11:37:00+03:00");
        test.put("finish", "2023-09-04T11:50:00+03:00");
        test.put("comment", "Successful execution1");
        if (isPassed)
            test.put("status", "PASSED");
        else
            test.put("status", "FAILED");
        tests.add(test);
    }

    public void sendTestResults() throws IOException {
        String endpoint = "https://xray.cloud.getxray.app/api/v1/import/execution";
        String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZW5hbnQiOiJiYmJhNjMyZi1kZDI3LTM0YjctODdlMi0xMjM1NjZkMzRlZDIiLCJhY2NvdW50SWQiOiI2NDAwODZjMDljYmE3Y2EwMjg3N2M2ZGMiLCJpc1hlYSI6ZmFsc2UsImlhdCI6MTY3OTk4NzUxMCwiZXhwIjoxNjgwMDczOTEwLCJhdWQiOiI3REE2RTlFNEMzM0U0MzY5QkNFNTQzNTFBOTNGODJCRSIsImlzcyI6ImNvbS54cGFuZGl0LnBsdWdpbnMueHJheSIsInN1YiI6IjdEQTZFOUU0QzMzRTQzNjlCQ0U1NDM1MUE5M0Y4MkJFIn0.CL8yihp8_9d_dKdc3HQ5qzUqlvrp7ceenL9puc-u1Fc";

        // create the request body
        JSONObject requestBody = new JSONObject();
        JSONObject info = new JSONObject();
        info.put("summary", "Execution of automated tests for release");
        info.put("description", "This execution is automatically created when importing execution results from an external source");
        info.put("startDate", "2023-09-03T11:37:00+03:00");
        info.put("finishDate", "2023-09-04T11:50:00+03:00");
        requestBody.put("info", info);

        requestBody.put("tests", tests);

        // create the OkHttp client and request
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url(endpoint)
                .addHeader("Authorization", "Bearer " + bearerToken)
                .post(body)
                .build();

        // send the request and get the response
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseBody);

        // handle the response
        if (response.isSuccessful()) {
            System.out.println("Test execution created with key: " + jsonResponse.getString("key"));
        } else {
            System.out.println("Error creating test execution: " + jsonResponse.getString("error"));
        }
    }
}
