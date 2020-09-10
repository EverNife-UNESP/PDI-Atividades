package br.com.finalcraft.unesp.java.pdi;

import br.com.finalcraft.unesp.java.pdi.data.image.FileHelper;
import br.com.finalcraft.unesp.java.pdi.data.wrapper.ImgWrapper;
import br.com.finalcraft.unesp.java.pdi.javafx.controller.MainController;

import java.io.File;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class SerialActions {

    public static ImgWrapper root;
    public static ImgWrapper temp;

    public static ActionEnum getByName(String name){
        return ActionEnum.valueOf(name.toUpperCase());
    }

    private static double[][] criarPessos(int tamanho){
        double[][] pesos = new double[tamanho][tamanho];
        for (int i = 0; i < pesos.length; i++) {
            for (int j = 0; j < pesos.length; j++) {
                pesos[i][j] = 1;
            }
        }
        return pesos;
    }

    private static double[][] criarPesosLaplaciano(int mode){
        double[][] pesos = new double[3][3];
        switch (mode){
            case 1:{
                pesos = new double[][]{
                        {0,-1,0},
                        {-1,4,-1},
                        {0,-1,0},
                };
                break;
            }
            case 2:{
                pesos = new double[][]{
                        {0,1,0},
                        {1,-4,1},
                        {0,1,0},
                };
                break;
            }
            case 3:{
                pesos = new double[][]{
                        {-1,-1,-1},
                        {-1,8,-1},
                        {-1,-1,-1},
                };
                break;
            }
            case 4:{
                pesos = new double[][]{
                        {1,1,1},
                        {1,-8,1},
                        {1,1,1},
                };
                break;
            }
        }
        return pesos;
    }

    public static enum ActionEnum {
        READ(args -> {
            root = FileHelper.readAndCreateImageWrapper(new File(args[0]));
        }, "<FilePath> - Ler Arquivo"),
        EXPORT(args -> {
            FileHelper.export(new File(args[0]), root);
        }, "<FilePath> - Exportar Arquivo"),
        TEMP_SAVE(args -> {
            MainController.leftImage = root.clone();
        }, "- Salvar imagem no lado Esquerdo"),
        ROTATE_RIGHT(args -> {
            root = root.rotateRight();
        }, " - Rotacionar a Direita"),
        ROTATE_LEFT(args -> {
            root = root.rotateLeft();
        }, " - Rotacionar a Esquerda"),
        FLIP_HORIZONTAL(args -> {
            root = root.flipHorizontal();
        }, " - Girar imagem na Horizontal"),
        FLIP_VERTICAL(args -> {
            root = root.flipVertical();
        }, " - Girar imagem na Vertical"),
        INVERT_COLORS(args -> {
            root = root.inverse();
        }, " - Inverter Cores"),
        ADD_IMG(args -> {
            temp = FileHelper.readAndCreateImageWrapper(new File(args[0]));
            root = root.add(temp);
        }, "<FilePath> - Adicionar Imagem a imagem atual"),
        REMOVE_IMG(args -> {
            temp = FileHelper.readAndCreateImageWrapper(new File(args[0]));
            root = root.subtract(temp);
        }, "<FilePath> - Remover Imagem da imagem atual"),
        ADD_VALUE(args -> {
            root = root.setBright(Integer.valueOf(args[0]));
        }, "<valor> - Adicionar valor a todos os pixels"),
        ADD_VALUE_IN_RANGE(args -> {
            Integer valor = Integer.valueOf(args[0]);
            Integer minBound = Integer.valueOf(args[1].split(Pattern.quote("-"))[0]);
            Integer maxBound = Integer.valueOf(args[1].split(Pattern.quote("-"))[0]);
            Integer valorExterno = args.length >= 3 ? Integer.valueOf(args[2]) : 0;
            root = root.setBright(valor, minBound, maxBound);
            if (valorExterno != 0){
                root = root.setBright(valorExterno, 0, minBound - 1);
                root = root.setBright(valorExterno, maxBound + 1, root.getRed().LIMIAR);
            }
        }, "<valor> <START-END> [valor]- Adicionar valor a todos os pixels dentro do intervalo especificado, e, caso o segundo argumento esteja definido, adicionar o segundo valor aos pixels fora do intervalo."),
        REMOVE_VALUE(args -> {
            root = root.setBright(-Integer.valueOf(args[0]));
        }, "<valor> - Remover valor de todos os pixels"),
        MULTIPLY_VALUE(args -> {
            root = root.multiply(Double.valueOf(args[0]));
        }, "<valor> - Multiplicar todos os pixels"),
        DIVIDE_VALUE(args -> {
            root = root.multiply(1D / Double.valueOf(args[0]));
        }, "<valor> - Dividir todos os pixels"),
        FILTER_AVARAGE(args -> {
            Integer tam = Integer.parseInt(args[0]);
            double[][] pesos = criarPessos(tam);
            root = root.filtragemEspacialMedia(1D/(pesos.length ^ 2), pesos);
        }, "<tamanho> - Aplicar filtro da MÉDIA"),
        FILTER_MEDIAN(args -> {
            Integer tam = Integer.parseInt(args[0]);
            double[][] pesos = criarPessos(tam);
            root = root.filtragemEspacialMediana(pesos);
        }, "<tamanho> - Aplicar filtro da MEDIANA"),
        FILTER_LAPLACIAN(args -> {
            Integer modo = Integer.parseInt(args[0]);
            double[][] pesos = criarPesosLaplaciano(modo);
            root = root.filtragemEspacialLaplaciana(pesos);
        }, "<1|2|3|4> - Aplicar filtro LAPLACIANO (Centro 4, -4, 8, -8) respectivamente"),
        FILTER_HIGHBOOST(args -> {
            Double constante = Double.parseDouble(args[0]);
            Integer tam = Integer.parseInt(args[0]);
            double[][] pesos = criarPessos(tam);
            root = root.filtragemEspacialHighBoost(constante, pesos);
        }, "<tamanho> - Aplicar filtro HIGH BOST"),
        EQUALIZATION(args -> {
            root = root.equalizarBrilho();
        }, "- Aplicar Equalização de Brilho"),
        EXTRACT_CHANNEL(args -> {
            char channel = args[0].toLowerCase().charAt(0);
            switch (channel) {
                case 'r':
                    root = root.extractRed();
                    break;
                case 'g':
                    root = root.extractGreen();
                    break;
                case 'b':
                    root = root.extractBlue();
                    break;
                case 'c':
                    root = root.extractCyan();
                    break;
                case 'm':
                    root = root.extractMagenta();
                    break;
                case 'y':
                    root = root.extractYellow();
                    break;
                case 'h':
                    root = root.extractHue();
                    break;
                case 's':
                    root = root.extractSaturation();
                    break;
                case 'i':
                    root = root.extractIntensity();
                    break;
            }
        }, "<r|g|b|c|m|y|h|s|i> - Extrair Canal da imagem"),
        COLORIFY(args -> {
            root = root.colorify();
        }, "- Colorir Imagem"),
        ;


        private final Consumer<String[]> action;
        private final String usage;

        ActionEnum(Consumer<String[]> action, String usage) {
            this.action = action;
            this.usage = usage;
        }

        public void execute(String[] args){
            action.accept(args);
        }

        public String getUsage() {
            return usage;
        }
    }
}
