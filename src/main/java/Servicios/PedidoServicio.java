package servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import dtos.PedidoDto;
import org.springframework.stereotype.Service;

@Service
public class PedidoServicio {

    private static final String API_URL = "http://localhost:8081/api/pedidos";  // Asegúrate de usar la URL correcta de tu API

    public String crearPedido(dtos.PedidoDto pedidoDto) throws Exception {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInputString = objectMapper.writeValueAsString(pedidoDto);

            // Imprimir el JSON antes de enviarlo
            System.out.println("JSON enviado: " + jsonInputString);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                return "Pedido creado correctamente";
            } else {
                InputStream errorStream = connection.getErrorStream();
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(errorStream))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                }
                return "Error al crear el pedido, código de respuesta: " + responseCode + ", detalle: " + response.toString();
            }

        } catch (Exception e) {
            throw new Exception("Error al enviar la solicitud al servidor: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}