package view;

import java.util.function.Function;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Class for adding buttons into TableView cells 
 * Source:
 * https://stackoverflow.com/questions/29489366/how-to-add-button-in-javafx-table-view
 *
 * @author Kerkko
 */
public class TableViewHelper<S> extends TableCell<S, Button> {

	private final Button actionButton;

	public TableViewHelper(String label, Function<S, S> function) {
		this.getStyleClass().add("action-button-table-cell");

		this.actionButton = new Button(label);
		this.actionButton.setOnAction((ActionEvent e) -> {
			function.apply(getCurrentItem());
		});
		this.actionButton.setMaxWidth(Double.MAX_VALUE);
	}

	public S getCurrentItem() {
		return (S) getTableView().getItems().get(getIndex());
	}

	public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label,
			Function<S, S> function) {
		return param -> new TableViewHelper<>(label, function);
	}

	@Override
	public void updateItem(Button item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setGraphic(null);
		} else {
			setGraphic(actionButton);
		}
	}
}