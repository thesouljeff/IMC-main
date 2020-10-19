package dad.maven.imc;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

/**
Clase para la practica IMC
*/
public class IMC extends Application {
	
	private StringProperty tfPesoProperty = new SimpleStringProperty(this, "peso");
	private DoubleProperty pesoValor = new SimpleDoubleProperty(this, "pesoValor");
	
	private StringProperty tfAlturaProperty = new SimpleStringProperty(this, "altura");
	private DoubleProperty alturaValor = new SimpleDoubleProperty(this, "alturaValor");
	
	private StringProperty lIMCProperty = new SimpleStringProperty(this, "imc");
	private DoubleProperty imcValor = new SimpleDoubleProperty(this, "imcValor");
	
	private StringProperty lResultadoProperty = new SimpleStringProperty(this, "resultado");
	
	public final double BAJO_PESO = 18.5;
	public final double NORMAL = 25;
	public final double SOBREPESO = 30;
	
	/**
	 * Método encargado de actualizar los labels de la aplicación
	 * y mostrar tanto el Indice como su valor (Peso bajo, Indice normal, sobrepeso u obesidad.
	 */
	public void actualizarIndicadorIMC() {
		Double imc = imcValor.get();
		
		if (imc == 0d) {
			lIMCProperty.set("IMC: (peso * altura^ 2)");
			lResultadoProperty.set("Peso bajo | Indice Normal | Sobrepeso | Obesidad");
		} else {
			lIMCProperty.set("IMC: " + imc);
			if (imc < BAJO_PESO)
				lResultadoProperty.set("Peso bajo");
			else if (imc >= BAJO_PESO && imc < NORMAL)
				lResultadoProperty.set("Indice Normal");
			else if (imc >= NORMAL && imc < SOBREPESO)
				lResultadoProperty.set("Sobrepeso");
			else
				lResultadoProperty.set("Obesidad");
		}
	}
	
	/**
	 * Método encargado de obtener los valores de peso y altura para luego
	 * calcular el IMC. Se esperan dichos valores en cm y kg,
	 * luego se aplica una reconversión para poder aplicar la fórmula:
	 * 
	 * IMC = peso / altura^2
	 */
	private void recalcularIMC() {
		if ((pesoValor.get() == 0d) || (alturaValor.get() == 0d))
			imcValor.set(0d);
		else {
			Double p = pesoValor.get();
			Double a = alturaValor.get();
			Double result = (p / (a * a)) * 10000d;
			imcValor.set(Math.round(result * 100.0) / 100.0);
		}
	}

	/**
	 * Método encargado de construir los aspectos básicos de la GUI.
	 *  
	 * @param primaryStage
	 * @throws Exception
	 */
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Declaración de componentes básicos
		Label lPeso = new Label("Peso:");
		Label lAltura = new Label("Altura:");
		Label lKg = new Label("kg");
		Label lCm = new Label("cm");
		
		Label lIMC = new Label("IMC: (peso * altura^ 2)");
		Label lResultado = new Label("Peso bajo | Indice Normal | Sobrepeso | Obesidad");
		
		TextField tfPeso = new TextField();
		tfPeso.setPrefColumnCount(3);
		tfPeso.setMaxWidth(100);
		tfPeso.setAlignment(Pos.CENTER);
		
		TextField tfAltura = new TextField();
		tfAltura.setPrefColumnCount(3);
		tfAltura.setMaxWidth(100);
		tfAltura.setAlignment(Pos.CENTER);		
		
		// Creamos los bindings entre componentes
		
		tfPesoProperty.bindBidirectional(tfPeso.textProperty());
		Bindings.bindBidirectional(tfPesoProperty, pesoValor, new NumberStringConverter());
		pesoValor.addListener((o, ov, nv) -> recalcularIMC());
		
		tfAlturaProperty.bindBidirectional(tfAltura.textProperty());
		Bindings.bindBidirectional(tfAlturaProperty, alturaValor, new NumberStringConverter());
		alturaValor.addListener((o, ov, nv) -> recalcularIMC());
		
		lIMCProperty.bindBidirectional(lIMC.textProperty());
		lResultadoProperty.bindBidirectional(lResultado.textProperty());
		imcValor.addListener((o, ov, nv) -> actualizarIndicadorIMC());
		
		// Enlace de los componentes con los layouts asociados
		
		HBox hbPeso = new HBox();
		hbPeso.setSpacing(5);
		hbPeso.setAlignment(Pos.BASELINE_CENTER);
		hbPeso.getChildren().addAll(lPeso, tfPeso, lKg);
		
		HBox hbAltura = new HBox();
		hbAltura.setSpacing(5);
		hbAltura.setAlignment(Pos.BASELINE_CENTER);
		hbAltura.getChildren().addAll(lAltura, tfAltura, lCm);
		
		HBox hbImc = new HBox();
		hbImc.setSpacing(1);
		hbImc.setAlignment(Pos.BASELINE_CENTER);
		
		VBox root = new VBox();
		root.setSpacing(5);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(hbPeso, hbAltura, hbImc, lIMC, lResultado);
		
		Scene escena = new Scene(root, 320, 200);
		
		primaryStage.setScene(escena);
		primaryStage.setTitle("IMC");
		primaryStage.show();

	}

	/**
	 * Método main encargado de iniciar la aplicación.
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
