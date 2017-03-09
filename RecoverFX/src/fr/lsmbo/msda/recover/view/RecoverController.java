package fr.lsmbo.msda.recover.view;

import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;


import fr.lsmbo.msda.recover.io.PeaklistRecovered;
import fr.lsmbo.msda.recover.Main;
import fr.lsmbo.msda.recover.Session;
import fr.lsmbo.msda.recover.Views;
import fr.lsmbo.msda.recover.filters.IdentifiedSpectraFilter;
import fr.lsmbo.msda.recover.gui.Recover;
import fr.lsmbo.msda.recover.io.PeaklistReader;
import fr.lsmbo.msda.recover.lists.Filters;
import fr.lsmbo.msda.recover.lists.ListOfSpectra;
import fr.lsmbo.msda.recover.lists.Spectra;
import fr.lsmbo.msda.recover.model.Spectrum;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RecoverController {

	private final int SIZE_COL_ID = 50;
//	private final int SIZE_COL_TITLE = 200;
	private final int SIZE_COL_MOZ = 100;
	private final int SIZE_COL_INTENSITY = 100;
	private final int SIZE_COL_CHARGE = 75;
	private final int SIZE_COL_RT = 75;
	private final int SIZE_COL_NBFRAGMENTS = 100;
	private final int SIZE_COL_UPN = 75;
	private final int SIZE_COL_IDENTIFIED = 75;
	private final int SIZE_COL_RECOVERED = 75;
	
	private Stage dialogStage;
//	private JFreeChart chart;
	private SpectrumChart spectrumChart;
	

//	@FXML
//	private MenuItem mnLoadPeaklist;
	@FXML
	private MenuItem mnLoadFirstPeaklist;
	@FXML
	private MenuItem mnLoadSecondPeaklist;
	@FXML
	private MenuItem mnExportFirstPeaklist;
	@FXML
	private MenuItem mnExportSecondPeaklist;
//	@FXML
//	private MenuItem mnExportPeaklist;
	@FXML
	private MenuItem mnExportBatch;
	@FXML
	private MenuItem mnQuit;
	@FXML
	private MenuItem mnOpenParsingRulesManager;
	@FXML
	private MenuItem mnOpenFilterManager;
	@FXML
	private CheckMenuItem mnUseFixedAxis;
	@FXML
	private CheckMenuItem mnCheckRecoverForIdentified;
	@FXML
	private CheckMenuItem mnUncheckRecoverForIdentified;
	@FXML
	private CheckMenuItem mnCheckRecoverForNonIdentified;
	@FXML
	private CheckMenuItem mnUncheckRecoverForNonIdentified;
	@FXML
	private MenuItem mnResetRecover;
	@FXML
	public TableView<Spectrum> table;
	@FXML
	private TableView<Spectrum> table1;
	@FXML
	private TableColumn<Spectrum, Integer> colId;
	@FXML
	private TableColumn<Spectrum, Integer> colId1;
	@FXML
	private TableColumn<Spectrum, String> colTitle;
	@FXML
	private TableColumn<Spectrum, String> colTitle1;
	@FXML
	private TableColumn<Spectrum, Float> colMoz;
	@FXML
	private TableColumn<Spectrum, Float> colMoz1;
	@FXML
	private TableColumn<Spectrum, Float> colInt;
	@FXML
	private TableColumn<Spectrum, Float> colInt1;
	@FXML
	private TableColumn<Spectrum, Integer> colCharge;
	@FXML
	private TableColumn<Spectrum, Integer> colCharge1;
	@FXML
	private TableColumn<Spectrum, Float> colRT;
	@FXML
	private TableColumn<Spectrum, Float> colRT1;
	@FXML
	private TableColumn<Spectrum, Integer> colNbFragments;
	@FXML
	private TableColumn<Spectrum, Integer> colNbFragments1;
	@FXML
	private TableColumn<Spectrum, Integer> colUPN;
	@FXML
	private TableColumn<Spectrum, Integer> colUPN1;
	@FXML
	private TableColumn<Spectrum, Boolean> colIdentified;
	@FXML
	private TableColumn<Spectrum, Boolean> colIdentified1;
	@FXML
	private TableColumn<Spectrum, Boolean> colRecover;
	@FXML
	private TableColumn<Spectrum, Boolean> colRecover1;
//	@FXML
//	private SplitPane bottomPanel;
	@FXML
	private AnchorPane filterAnchor;
	@FXML
	private AnchorPane filterAnchor1;
	
//	@FXML
//	private AnchorPane chartAnchor;
	@FXML
	private SwingNode swingNodeForChart;
	@FXML
	private SwingNode swingNodeForChart1;
	
	@FXML
	private void initialize() {
		
		//Left view
		Spectra spectra = ListOfSpectra.getFirstSpectra();
		// define spectrum list
		table.setItems(spectra.getSpectraAsObservable());
        colId.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("title"));
		colMoz.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("mz"));
		colInt.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("intensity"));
		colCharge.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("charge"));
		colRT.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("retentionTime"));
		colNbFragments.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("nbFragments"));
		colUPN.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("upn"));
		colIdentified.setCellValueFactory(new PropertyValueFactory<Spectrum, Boolean>("isIdentified"));
		colRecover.setCellValueFactory(new PropertyValueFactory<Spectrum, Boolean>("isRecover"));
		// set column sizes
		colId.setPrefWidth(SIZE_COL_ID);
		colMoz.setPrefWidth(SIZE_COL_MOZ);
		colInt.setPrefWidth(SIZE_COL_INTENSITY);
		colCharge.setPrefWidth(SIZE_COL_CHARGE);
		colRT.setPrefWidth(SIZE_COL_RT);
		colNbFragments.setPrefWidth(SIZE_COL_NBFRAGMENTS);
		colUPN.setPrefWidth(SIZE_COL_UPN);
		colIdentified.setPrefWidth(SIZE_COL_IDENTIFIED);
		colRecover.setPrefWidth(SIZE_COL_RECOVERED);
		colTitle.prefWidthProperty().bind(table.widthProperty().subtract(SIZE_COL_ID+SIZE_COL_MOZ+SIZE_COL_INTENSITY+SIZE_COL_CHARGE+SIZE_COL_RT+SIZE_COL_NBFRAGMENTS+SIZE_COL_UPN+SIZE_COL_IDENTIFIED+SIZE_COL_RECOVERED+0));

		mnUseFixedAxis.setSelected(Session.USE_FIXED_AXIS);
		filterAnchor.setPrefWidth(100);
		table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//			// set new data and title
//			chart.setData(SpectrumChart.getData(newSelection));
//			chart.setTitle(newSelection.getTitle());
//			// reset axis values  because autoranging is off (necessary to allow fixed axis)
//			resetChartAxis(newSelection);
			
//			chart = SpectrumChart.getPlot(newSelection);
			spectrumChart = new SpectrumChart(newSelection);
	        ChartPanel chartPanel = new ChartPanel(spectrumChart.getChart());
			 SwingUtilities.invokeLater(new Runnable() {
				 @Override
				 public void run() {
					 swingNodeForChart.setContent(chartPanel);
				 }
			 });
		});	
//		table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
//			
//		});

		
		//Right view
		Spectra secondSpectra = ListOfSpectra.getSecondSpectra();
		// define spectrum list
		table1.setItems(secondSpectra.getSpectraAsObservable());
        colId1.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("id"));
        colTitle1.setCellValueFactory(new PropertyValueFactory<Spectrum, String>("title"));
		colMoz1.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("mz"));
		colInt1.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("intensity"));
		colCharge1.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("charge"));
		colRT1.setCellValueFactory(new PropertyValueFactory<Spectrum, Float>("retentionTime"));
		colNbFragments1.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("nbFragments"));
		colUPN1.setCellValueFactory(new PropertyValueFactory<Spectrum, Integer>("upn"));
		colIdentified1.setCellValueFactory(new PropertyValueFactory<Spectrum, Boolean>("isIdentified"));
		colRecover1.setCellValueFactory(new PropertyValueFactory<Spectrum, Boolean>("isRecover"));
		// set column sizes
		colId1.setPrefWidth(SIZE_COL_ID);
		colMoz1.setPrefWidth(SIZE_COL_MOZ);
		colInt1.setPrefWidth(SIZE_COL_INTENSITY);
		colCharge1.setPrefWidth(SIZE_COL_CHARGE);
		colRT1.setPrefWidth(SIZE_COL_RT);
		colNbFragments1.setPrefWidth(SIZE_COL_NBFRAGMENTS);
		colUPN1.setPrefWidth(SIZE_COL_UPN);
		colIdentified1.setPrefWidth(SIZE_COL_IDENTIFIED);
		colRecover1.setPrefWidth(SIZE_COL_RECOVERED);
		colTitle1.prefWidthProperty().bind(table1.widthProperty().subtract(SIZE_COL_ID+SIZE_COL_MOZ+SIZE_COL_INTENSITY+SIZE_COL_CHARGE+SIZE_COL_RT+SIZE_COL_NBFRAGMENTS+SIZE_COL_UPN+SIZE_COL_IDENTIFIED+SIZE_COL_RECOVERED+0));

		mnUseFixedAxis.setSelected(Session.USE_FIXED_AXIS);
		filterAnchor1.setPrefWidth(100);
		table1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//			// set new data and title
//			chart.setData(SpectrumChart.getData(newSelection));
//			chart.setTitle(newSelection.getTitle());
//			// reset axis values  because autoranging is off (necessary to allow fixed axis)
//			resetChartAxis(newSelection);
			
//			chart = SpectrumChart.getPlot(newSelection);
			spectrumChart = new SpectrumChart(newSelection);
	        ChartPanel chartPanel = new ChartPanel(spectrumChart.getChart());
			 SwingUtilities.invokeLater(new Runnable() {
				 @Override
				 public void run() {
					 swingNodeForChart1.setContent(chartPanel);
				 }
			 });
		});	
		
		//CONTEXT MENU && MENU ITEMS
		ContextMenu mozContext = new ContextMenu();
		MenuItem mozItem = new MenuItem("Filter by m/z...");
		mozContext.getItems().add(mozItem);
		colMoz.setContextMenu(mozContext);
		mozItem.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Views.FILTER_MOZ);
					AnchorPane page = (AnchorPane) loader.load();
					Stage dialogStageMoz = new Stage();
					dialogStageMoz.setTitle("Apply Filters");
					dialogStageMoz.initModality(Modality.WINDOW_MODAL);
					dialogStageMoz.initOwner(dialogStage);
					Scene scene = new Scene(page);
					dialogStageMoz.setScene(scene);
					FilterByMozController controller = loader.getController();
					controller.setDialogStage(dialogStageMoz);
					dialogStageMoz.showAndWait();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		ContextMenu intensityContext = new ContextMenu();
		MenuItem intensityItem = new MenuItem("Filter by precursor intensity...");
		intensityContext.getItems().add(intensityItem);
		colInt.setContextMenu(intensityContext);
		intensityItem.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Views.FILTER_INTENSITY);
					AnchorPane page = (AnchorPane) loader.load();
					Stage dialogStage = new Stage();
					dialogStage.setTitle("Precursor intensity");
					dialogStage.initModality(Modality.WINDOW_MODAL);
					Scene scene = new Scene(page);
					dialogStage.setScene(scene);
					dialogStage.showAndWait();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		ContextMenu chargeContext = new ContextMenu();
		MenuItem chargeItem = new MenuItem("Filter by charge...");
		chargeContext.getItems().add(chargeItem);
		colCharge.setContextMenu(chargeContext);
		chargeItem.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Views.FILTER_CHARGE);
					AnchorPane page = (AnchorPane) loader.load();
					Stage dialogStage = new Stage();
					dialogStage.setTitle("Charge");
					dialogStage.initModality(Modality.WINDOW_MODAL);
					Scene scene = new Scene(page);
					dialogStage.setScene(scene);
					dialogStage.showAndWait();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		ContextMenu retentionTimeContext = new ContextMenu();
		MenuItem retentionTimeItem = new MenuItem("Filter by RT...");
		retentionTimeContext.getItems().add(retentionTimeItem);
		colRT.setContextMenu(retentionTimeContext);
		retentionTimeItem.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Views.FILTER_RETENTION_TIME);
					AnchorPane page = (AnchorPane) loader.load();
					Stage dialogStage = new Stage();
					dialogStage.setTitle("Retention Time");
					dialogStage.initModality(Modality.WINDOW_MODAL);
					Scene scene = new Scene(page);
					dialogStage.setScene(scene);
					dialogStage.showAndWait();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		ContextMenu nbFragmentContext = new ContextMenu();
		MenuItem nbFragmentItem = new MenuItem("Filter by number of fragment...");
		nbFragmentContext.getItems().add(nbFragmentItem);
		colNbFragments.setContextMenu(nbFragmentContext);
		nbFragmentItem.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event){
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Views.FILTER_NB_FRAGMENT);
					AnchorPane page = (AnchorPane) loader.load();
					Stage dialogStage = new Stage();
					dialogStage.setTitle("Number of fragment");
					dialogStage.initModality(Modality.WINDOW_MODAL);
					Scene scene = new Scene(page);
					dialogStage.setScene(scene);
					dialogStage.showAndWait();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		
//		chartAnchor.getChildren().add(swingNodeForChart);
		
//		defineChartMenu();
	}
	
//	@FXML
//	private void handleMousePressed(MouseEvent m) {
//		if(m.isPrimaryButtonDown()) {
//			selectedXValue = m.getX() - chart.getXAxis().getLayoutX(); // important to subtract layoutX to avoid a shift
//			rectZoom.setWidth(0);
//			rectZoom.setHeight(0);
//		}
//	}
//	
//	@FXML
//	private void handleMouseDragged(MouseEvent m) {
//		if(m.isPrimaryButtonDown()) {
//			double x = m.getX() - chart.getXAxis().getLayoutX();
//			// FIXME restraining movement to the chart area does not work (only on the right)
//	        rectZoom.setX(Math.min(x, selectedXValue));
//	        rectZoom.setWidth(Math.abs(x - selectedXValue));
//	        rectZoom.setHeight(chart.getYAxis().getHeight());
//			rectZoom.setVisible(true);
//		}
//	}
//	
//	@FXML
//	private void handleMouseReleased(MouseEvent m) {
//        // zoom
//		rectZoom.setWidth(0);
//		rectZoom.setHeight(0);
//		rectZoom.setVisible(false);
//		if(m.getButton().equals(MouseButton.SECONDARY)) {
//			// display a menu with some actions (such as reset zoom, fixed axis, filters...)
//			chartMenu.show(chart, m.getScreenX(), m.getScreenY());
//		}
//	}
	
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
	private FileChooser getFileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a peaklist file");
		// default folder is 'Documents'
		File initialDirectory = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Documents");
		// if it does not exist, then it's home folder
		if(!initialDirectory.exists())
			initialDirectory = new File(System.getProperty("user.home"));
		// if a file is already loaded then it's the same folder
		if(Session.CURRENT_FILE != null)
			initialDirectory = Session.CURRENT_FILE.getParentFile();
		fileChooser.setInitialDirectory(initialDirectory);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All peaklists files", "*.*"),
                new FileChooser.ExtensionFilter("MGF", "*.mgf"),
                new FileChooser.ExtensionFilter("PKL", "*.pkl")
            );
		return fileChooser;
	}
	
	//Action to load the first table with a peaklist
	@FXML
	private void handleClickMenuLoadFirst() {
		FileChooser fileChooser = getFileChooser();
		File file = fileChooser.showOpenDialog(this.dialogStage);
		if(file != null) {
			Recover.useSecondPeaklist = false;
			loadFile(file);
//			Filter f = new Filter();
//			f.applyFilters();
		}
	}
	
	//Action to load the second table with a peaklist
	@FXML
	private void handleClickMenuLoadSecond() {
		FileChooser fileChooser = getFileChooser();
		File file = fileChooser.showOpenDialog(this.dialogStage);
		if(file != null) {
			Recover.useSecondPeaklist = true;
			loadFile(file);
//			Filter f = new Filter();
//			f.applyFilters();
		}
	}
	public void loadFile(File selectedFile) {
		long startTime = System.currentTimeMillis();
		PeaklistReader.load(selectedFile);
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("ABU loading time: "+(double)totalTime/1000+" sec");
		System.out.println("ABU "+ListOfSpectra.getFirstSpectra().getNbSpectra()+" spectra");
		System.out.println("ABU "+ListOfSpectra.getSecondSpectra().getNbSpectra()+" spectra");
		initialize();
		this.dialogStage.setTitle(Main.recoverTitle());
		if(PeaklistReader.retentionTimesNotFound()) {
			// open a dialogbox to warn the user that he should try other parsing rules
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Retention times are missing");
			alert.setHeaderText("Retention times could not be extracted from titles, do you want to open the Parsing rules selection list ?");
			ButtonType btnYes = new ButtonType("Yes", ButtonData.YES);
			ButtonType btnNo = new ButtonType("No", ButtonData.NO);
			alert.getButtonTypes().setAll(btnYes, btnNo);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == btnYes){
				System.out.println("yes");
				handleClickMenuParsingRules();
			} else {
				System.out.println("no");
			}
			
		}
	}
	
	@FXML
	private void handleClickMenuExportFirst() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save your new peaklist");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("MGF", "*.mgf"),
				new ExtensionFilter("PKL","*.pkl"));
		File savedFile = fileChooser.showSaveDialog(this.dialogStage);
		if (savedFile !=null)
			Recover.useSecondPeaklist = false;
			PeaklistRecovered.save(savedFile);
//		System.out.println(bottomPanel.getDividerPositions()[0]);
		
	}
	
	@FXML
	private void handleClickMenuExportSecond() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save your new peaklist");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("MGF", "*.mgf"),
				new ExtensionFilter("PKL","*.pkl"));
		File savedFile = fileChooser.showSaveDialog(this.dialogStage);
		if (savedFile !=null)
			Recover.useSecondPeaklist = true;
			PeaklistRecovered.save(savedFile);
//		System.out.println(bottomPanel.getDividerPositions()[0]);
		
	}
	
	@FXML
	private void handleClickMenuBatch() {
	}
	
	@FXML
	private void handleClickMenuQuit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit Recover ?");
		alert.setHeaderText("Are you sure ?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			System.exit(0);
		}
	}
	
	@FXML
	private void handleClickMenuFilters() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Views.FILTER);
			BorderPane page = (BorderPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Apply Filters");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(this.dialogStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			FilterController2 controller = loader.getController();
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
			table.refresh();
			table1.refresh();
		} catch(IOException e) {
			e.printStackTrace();
			
		}
	}
	
	@FXML
	private void handleClickMenuParsingRules() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Views.PARSING_RULES);
			BorderPane page = (BorderPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Parsing rules");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(this.dialogStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			ParsingRulesController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
			table.refresh();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void handleClickMenuFixedAxis() {
		Session.USE_FIXED_AXIS = !Session.USE_FIXED_AXIS;
		resetChartAxis(table.getSelectionModel().getSelectedItem());
		if(spectrumChart != null) {
			spectrumChart.changeAxisRange();
		}
	}
	@FXML
	private void handleClickMenuResetRecover(){
		//TODO move the loop in a new class
		for (Spectrum sp : ListOfSpectra.getFirstSpectra().getSpectraAsObservable()){
			sp.setIsRecover(false);
		}
		table.refresh();
		Filters.resetHashMap();
	}
	
	@FXML
	private void handleCheckRecoverForIdentified(){
		if(mnCheckRecoverForIdentified.isSelected()){
			mnUncheckRecoverForIdentified.setSelected(false);
			IdentifiedSpectraFilter.setCheckRecoverIdentified(true);
			IdentifiedSpectraFilter.setUncheckRecoverIdentified(false);
		}
		else{
			IdentifiedSpectraFilter.setCheckRecoverIdentified(false);
		}
	}
	
	@FXML
	private void handleUncheckRecoverForIdentified(){
		if(mnUncheckRecoverForIdentified.isSelected()){
			mnCheckRecoverForIdentified.setSelected(false);
			IdentifiedSpectraFilter.setUncheckRecoverIdentified(true);
			IdentifiedSpectraFilter.setCheckRecoverIdentified(false);
		}
		else
			IdentifiedSpectraFilter.setUncheckRecoverIdentified(false);
	}
	
	@FXML
	private void handleCheckRecoverForNonIdentified(){
		if(mnCheckRecoverForNonIdentified.isSelected()){
			mnUncheckRecoverForNonIdentified.setSelected(false);
			IdentifiedSpectraFilter.setCheckRecoverNonIdentified(true);
			IdentifiedSpectraFilter.setUncheckRecoverNonIdentified(false);
		}
		else
			IdentifiedSpectraFilter.setCheckRecoverNonIdentified(false);
	}
	
	@FXML
	private void handleUncheckRecoverForNonIdentified(){
		if(mnUncheckRecoverForNonIdentified.isSelected()){
			mnCheckRecoverForNonIdentified.setSelected(false);
			IdentifiedSpectraFilter.setUncheckRecoverNonIdentified(true);
			IdentifiedSpectraFilter.setCheckRecoverNonIdentified(false);
		}
		else
			IdentifiedSpectraFilter.setUncheckRecoverNonIdentified(false);

	}
	
	@FXML
	private void test(){
	}
	
	private void resetChartAxis(Spectrum spectrum) {
//		if(chart.getData().size() > 0) {
//			xAxis.setLowerBound(0);
//			xAxis.setUpperBound(SpectrumChart.getUpperBound(spectrum.getFragmentMaxMoz(), true));
//			xAxis.setTickUnit(SpectrumChart.getTickUnit(xAxis.getUpperBound()));
//			yAxis.setLowerBound(0);
//			yAxis.setUpperBound(SpectrumChart.getUpperBound(spectrum.getFragmentMaxIntensity(), false));
//			yAxis.setTickUnit(SpectrumChart.getTickUnit(yAxis.getUpperBound()));
//		}
	}
	
//	private void defineChartMenu() {
//		// display a menu with some actions (such as reset zoom, fixed axis, filters...)
//		final MenuItem resetZoomItem = new MenuItem("Reset zoom");
//		resetZoomItem.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				System.out.println("Reset zoom");
//			}
//		});
//
//		chartMenu = new ContextMenu(
//		  resetZoomItem
////		  mnUseFixedAxis
//		);
//	}
}
