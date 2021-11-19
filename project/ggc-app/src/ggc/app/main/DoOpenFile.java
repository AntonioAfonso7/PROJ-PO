package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.WarehouseManager;
import ggc.app.exceptions.FileOpenFailedException;
import ggc.exceptions.UnavailableFileException;

/**
 * Open existing saved state.
 */
class DoOpenFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoOpenFile(WarehouseManager receiver) {
    super(Label.OPEN, receiver);
    addStringField("file", Prompt.openFile());
  }

  @Override
  public final void execute() throws CommandException {
    try {
      _receiver.load(stringField("file"));
    } catch (UnavailableFileException e) {
      throw new FileOpenFailedException(e.getFilename());
    }
  }
}
