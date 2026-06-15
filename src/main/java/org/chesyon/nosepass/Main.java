package org.chesyon.nosepass;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.io.IOException;
import java.lang.System;
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws IOException {
        new Client();
        JDA jda = JDABuilder.createLight(System.getenv("BOT_TOKEN"), Collections.emptyList())
                .addEventListeners(new SlashCommandListener())
                .build();

        // Register your commands to make them visible globally on Discord:

        CommandListUpdateAction commands = jda.updateCommands();

        // Add all your commands on this action instance
        commands.addCommands(
                Commands.slash("map", "description")
                        .addOption(OptionType.STRING, "region", "Source region of the address", true)
                        .addOption(OptionType.STRING, "address", "Address to be converted (hex)", true)
                        .addOption(OptionType.STRING, "section", "Section of the address", false));

        // Then finally send your commands to discord using the API
        commands.queue();
    }
}
