package Bank;
import com.google.gson.*;
import java.lang.reflect.Type;

public class Serializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {
    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        // Store the class name for deserialization
        json.addProperty("CLASSNAME", src.getClass().getName());
        // Serialize the actual transaction object
        json.add("INSTANCE", context.serialize(src, src.getClass()));
        return json;
    }

    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Ensure the necessary keys are present in the JSON
        if (!jsonObject.has("CLASSNAME") || !jsonObject.has("INSTANCE")) {
            throw new JsonParseException("Invalid JSON structure: Missing required keys 'CLASSNAME' or 'INSTANCE'.");
        }

        // Extract CLASSNAME and INSTANCE for further processing
        String className = jsonObject.get("CLASSNAME").getAsString();
        JsonElement instanceData = jsonObject.get("INSTANCE");

        // Use the CLASSNAME value to determine the appropriate class for deserialization
        switch (className) {
            case "Bank.Payment":
                return context.deserialize(instanceData, Payment.class);
            case "Bank.IncomingTransfer":
                return context.deserialize(instanceData, IncomingTransfer.class);
            case "Bank.OutgoingTransfer":
                return context.deserialize(instanceData, OutgoingTransfer.class);
            default:
                throw new JsonParseException("Unexpected CLASSNAME: " + className);
        }
    }

}
