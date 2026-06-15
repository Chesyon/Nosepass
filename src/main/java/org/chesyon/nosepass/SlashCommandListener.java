package org.chesyon.nosepass;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;
import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.EmbedBuilder;

public class SlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "map" -> {
                @Nullable
                String region = event.getOption("region", OptionMapping::getAsString);
                @Nullable
                String addressStr = event.getOption("address", OptionMapping::getAsString);
                @Nullable
                String section = event.getOption("section", OptionMapping::getAsString);
                if (region == null)
                    replyProgramError(event, 0);
                else if (addressStr == null)
                    replyProgramError(event, 1);
                else {
                    // address cleanup
                    if (addressStr.toLowerCase().startsWith("0x"))
                        addressStr = addressStr.substring(2);
                    int address; // shuts up null type safety warning
                    try {
                        address = Integer.parseInt(addressStr, 16);
                    } catch (NumberFormatException e) {
                        replyInputError(event, "Please provide a hexadecimal offset.");
                        return;
                    }
                    // region cleanup
                    if (region.toLowerCase().equals("us"))
                        region = "na";
                    else
                        region = region.toLowerCase();
                    // The null check here only exists to shut up null type safety warning
                    if (region == null || (!region.equals("na") && !region.equals("eu") && !region.equals("jp"))) {
                        replyInputError(event, "Please choose between NA, EU, or JP for the region.");
                    } else {
                        // Create and send request to API
                        CompassRequest request = new CompassRequest(region, address, section);
                        CompassResponse response = Client.getCurrentClient().sendRequest(request);
                        // Ensure response was received
                        if (response == null)
                            replyProgramError(event, 2);
                        else {
                            // Send reply based on API response
                            EmbedBuilder eb = new EmbedBuilder();
                            String desc = "";
                            ArrayList<Issue> issues = response.getIssues();
                            ArrayList<String> sections = response.getSections();
                            for (Issue issue : issues)
                                desc += issueExplanation(issue, sections) + "\n";
                            if (response.getSucceeded()) {
                                eb.setTitle(String.format("0x%X [%s] in %s", address,
                                        region.toUpperCase(), sections.get(0)));
                                eb.setColor(Color.GREEN);
                                desc += String.format("**NA**: %s\n**EU**: %s\n**JP**: %s",
                                        response.getNa(),
                                        response.getEu(), response.getJp());
                            } else {
                                eb.setTitle("Could not map");
                                eb.setColor(Color.YELLOW);
                            }
                            eb.setDescription(desc);
                            event.replyEmbeds(eb.build()).queue();
                        }
                    }
                }
            }
        }
    }

    public static String issueExplanation(Issue issue, ArrayList<String> sections) {
        switch (issue) {
            case VERIFICATION_FAILED:
                return "Offset was not in the provided section.";
            case INVALID_SECTION:
                return "Provided section does not exist.";
            case FINDING_AUTOMATICALLY:
                return "Tried to find section automatically.";
            case NO_VALID_SECTIONS:
                return "Offset was not in any possible section.";
            case MULTIPLE_VALID_SECTIONS:
                String output = "Offset was in multiple possible sections:\n";
                for (String section : sections)
                    output += String.format("* %s\n", section);
                return output + "Please retry, specifying the desired section.";
        }
        return "Error! Failed to resolve Issue enum (how??)";
    }

    public void replyInputError(@Nonnull SlashCommandInteractionEvent event, String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Input issue");
        eb.setDescription(message);
        eb.setColor(Color.ORANGE);
        event.replyEmbeds(eb.build()).queue();
    }

    public void replyProgramError(@Nonnull SlashCommandInteractionEvent event, int code) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.setTitle(String.format("!! Error %d !!", code));
        switch (code) {
            case 0:
                eb.setDescription("region was null");
                break;
            case 1:
                eb.setDescription("addressStr was null");
                break;
            case 2:
                eb.setDescription("sky-compass gave a null response (did it crash?)");
                break;
            default:
                eb.setDescription("Error description for this number was missing. What???");
                break;
        }
        eb.setFooter("This wasn't supposed to happen, please report it!");
        event.replyEmbeds(eb.build()).queue();
    }
}