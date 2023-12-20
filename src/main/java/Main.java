import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Main {

    interface MyAiService {
        @UserMessage("{{it}}")
        String askAI(String userMessage);
    }

    static class Tools {
        @Tool("gets me current date, formatted as YYYY-mm-dd")
        String currentDate() {
            return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Tool("gets me current time, formatted as HH:MM:ss")
        String currentTime() {
            return LocalDateTime.now().format(DateTimeFormatter.ISO_TIME);
        }

    }

    public static void main(String[] args) {
        var model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("mistral")
                .temperature(0.0)
                .build();


        var chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        var s = AiServices.builder(MyAiService.class)
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .tools(new Tools())
                .build();

        System.out.println(s.askAI("hi, my name is Bob, whats your name?"));
        System.out.println(s.askAI("what is current date?"));
        System.out.println(s.askAI("whats my name?"));

    }
}
