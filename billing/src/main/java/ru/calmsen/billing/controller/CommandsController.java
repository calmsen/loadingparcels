package ru.calmsen.billing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.calmsen.billing.command.CommandSender;

import java.util.Map;

@RestController
@Tag(name = "Commands", description = "Выполняет команды")
@RequestMapping("api/v1/commands")
@RequiredArgsConstructor
public class CommandsController {
    private final CommandSender commandSender;

    /**
     * Выполняет команды
     *
     * @param commandName Название команды
     * @param args        Аргументы команды
     * @return результат выполнения команды
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Результат выполнения команды",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))})
    })
    @Operation(summary = "Детали счета")
    @PostMapping("{commandName}")
    public String send(
            @Parameter(description = "Название команды") @PathVariable("commandName") String commandName,
            @Parameter(description = "Аргументы команды") @RequestBody Map<String, String> args) {
        return commandSender.send(commandName, args);
    }
}
