package chiroito.cryostat;

import chiroito.cryostat.command.DeleteCommand;
import chiroito.cryostat.command.DumpCommand;
import chiroito.cryostat.command.ListCommand;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(name = "cryostat", subcommands = {DumpCommand.class, ListCommand.class, DeleteCommand.class})
public class CryostatCommand {
}
