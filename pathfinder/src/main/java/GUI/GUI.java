package GUI;

import filereader.Filereader;
import algorithms.AStar;
import algorithms.Dijkstra;
import algorithms.JPS;
import algorithms.BFS;
import datastructures.Node;
import datastructures.NodeList;
import datastructures.Tuple;
import datastructures.TupleList;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * the graphical user interface class
 * this class uses Javafx libraries to implement a graphical user interface
 * this class displays the maze as well as the shortest path
 * How to use:
 *           click on the desired starting location in the maze or input manually into the text field and click the confirm button to confirm the choice
 *           a red dot will appear to confirming the location as well as the location coordinates will appear in the upper left of the page
 *           repeat the process to confirm the destination
 *           click on the button 'run BFS' to run the BFS algorithm,the algorithm will run and the shortest path will be displayed in blue
 *           click on the button 'run Dijkstra' to run the Dijkstra algorithm, the shortest path will be displayed in red
 *           click on the button 'run A*' to run the A Star algorithm, the shortest path will be displayed in green
 *           click on the button 'run JPS' to run the Jump Point Search algorithm, the shortest path will be displayed in green
 *           the button 'reset' on the bottom left of the window will reset the map, sometimes it takes a while so be patient
 *           to change the map click on the drop down menu and select a map then click on the 'select' button
 * NOTE:
 *     
 *      
 *      
 *      
 *      BFS algorithm is for non-weighted graphs so there is only 4 directions of movement so the path will be longer than the other algorithms
 * 
 * 
 * 
 * @author alex
 */
public class GUI extends Application{
    
    private Tuple origin;
    private boolean origin_set_already = false;
    private boolean dest_set_already = false;
    private Tuple destination;
    private Pane maze;
    private Dijkstra dijk;
    private AStar Astar;
    private JPS jps;
    private BFS bfs;
    
    private int[][] maze_array_read_from_file;
    
    //the multiplicular amout to stretch the maze, only in int right now
    private int maze_stretch = 2;
    
    //maps
    private String m1 = "Mazes/maze512-1-0.map";
    private String m8 = "Mazes/maze512-8-0.map";
    private String m32 = "Mazes/maze512-32-0.map";
    private String r10 = "Mazes/random512-10-0.map";
    private String r40 = "Mazes/random512-40-7.map";
    
    private Filereader file;
    
    
    // stat Labels
    Label stats_title;
    Label stats_BFS;
    Label stats_BFS_Length;
    Label stats_BFS_Time;
    Label stats_dijk;
    Label stats_dijk_Length;
    Label stats_dijk_Time;
    Label stats_AStar;
    Label stats_AStar_Length;
    Label stats_AStar_Time;
    Label stats_JPS;
    Label stats_JPS_Length;
    Label stats_JPS_Time;

    // Buttons
    Button confirmOrigin;
    Button runBFS_Button;
    Button runDijkstra_Button;
    Button runAStar_Button;
    Button runJPS_button;
    Button maze_select_button;
    Button maze_reset_button;
    

    

    @Override
    public void start(Stage stage) throws Exception {
        this.initialize();
        
        
        //Create new Filereaderclass
        //it takes string input of the location of a .map maze file
        /**
         * the tested mazes are:
         *  Mazes/maze512-1-0.map 
         *  Mazes/maze512-32-0.map 
         *  Mazes/maze512-8-0.map
         *  Mazes/random512-10-0.map  
         *  Mazes/brc101d.map - 
         *  Mazes/random512-40-7.map 
        
        */
        //-------------------------------------------FILE PATH HERE--------------------------------------------------------------------
        this.file = new Filereader("Mazes/maze512-8-0.map");
        //------------------------------------------ABOVE HERE-------------------------------------------------------------------------
        

        //Filereader returns a 2d array of the maze
        this.maze_array_read_from_file= file.returnArray();
        // sets the class variable maze
        createMaze(maze_array_read_from_file);
        
        this.dijk = new Dijkstra(maze_array_read_from_file);
        
        this.Astar = new AStar(maze_array_read_from_file);
        
        this.jps = new JPS(maze_array_read_from_file);
        
        this.bfs = new BFS(maze_array_read_from_file);
        
        
        
        
        
        //the main borderpane where all of the nodes are set
        BorderPane borderpane = new BorderPane();
        borderpane.setRight(this.maze);
        // the top left VBox containing the text
        VBox top_left_vbox = new VBox();
        top_left_vbox.setSpacing(10);
        
        //the hbox containing the two textfields for the coordinates
        HBox coordinate_hbox = new HBox();
        TextField X_coordinate = new TextField("X =");
        TextField Y_coordinate = new TextField("Y =");
        
        coordinate_hbox.getChildren().add(X_coordinate);
        coordinate_hbox.getChildren().add(Y_coordinate);
        coordinate_hbox.setSpacing(20);
        
        //the vbox containing all of the stats from the algoritm performance and distance of shortest path
        VBox stats_vbox = new VBox();
        
        stats_vbox.getChildren().addAll(stats_title,stats_BFS,stats_BFS_Length,stats_BFS_Time,stats_dijk,stats_dijk_Length,stats_dijk_Time);
        stats_vbox.getChildren().addAll(stats_AStar,stats_AStar_Length,stats_AStar_Time,stats_JPS,stats_JPS_Length,stats_JPS_Time);
        
        FlowPane Direction_Origin_pane = new FlowPane();
        FlowPane Destination_pane = new FlowPane();
        
        Label Directions_label = new Label("click on the maze to set a starting location then press accept!");
        Label Origin_coordinates_label = new Label("Origin: X=  , Y=  ");
        Label Destination_coordinates_label = new Label("Destination: X=  , Y=  ");
        Direction_Origin_pane.getChildren().add(Directions_label);
        
        
        
        Label error_location_label = new Label();
        
        
        
        
        Direction_Origin_pane.getChildren().add(Origin_coordinates_label);
        Destination_pane.getChildren().add(Destination_coordinates_label);
        
        /**
         * event handler which sets the coordinates of the mouse on the maze to the X and Y coordinate TextField
         */
        this.maze.setOnMouseClicked(new EventHandler<MouseEvent>() {
        
            @Override
            public void handle(MouseEvent event){
                
                X_coordinate.setText("" + event.getX());
                Y_coordinate.setText("" + event.getY());
            }
        });
        
        
        /**
         * the button on activation first sets the class variable, Origin, with the values in the X, Y textfield 
         * the coordinate text updates with the confirmed values
         * a red circle gets set at the chosen location
         */
        confirmOrigin.setOnAction((event) -> {
            // mind the counterintuitive x, y values for the circle coordinates
            int x = (int)Double.parseDouble(Y_coordinate.getText()) / this.maze_stretch;
            int y = (int) Double.parseDouble(X_coordinate.getText())/this.maze_stretch;
            if(this.isValidLocation(x, y)){
                
                error_location_label.setText("");
            
            
                if(!this.origin_set_already){
                    setOrigin(new Tuple(x,y));
                    this.origin_set_already = true;
                    Origin_coordinates_label.setText("Origin: X= " + this.origin.y*this.maze_stretch + ", Y= " + this.origin.x*this.maze_stretch);
                    this.maze.getChildren().add(new Circle(this.origin.y*this.maze_stretch,this.origin.x*this.maze_stretch,4,Color.RED));
                }else if(!this.dest_set_already){
                    this.destination = new Tuple(x,y);
                    Destination_coordinates_label.setText("Destination: X= "+ this.destination.y*this.maze_stretch + ", Y= " + this.destination.x*this.maze_stretch);
                    this.dest_set_already = true;
                    this.maze.getChildren().add(new Circle(this.destination.y*this.maze_stretch,this.destination.x*this.maze_stretch,4,Color.RED));
                }
            }else{
                error_location_label.setText("Invalid location! Make sure you are not selecting a wall");
                error_location_label.setTextFill(Color.RED);
            }
            
            if(this.origin_set_already && this.dest_set_already){
                this.setAlgorithmButtonsEnabled();
                confirmOrigin.setDisable(true);
            }
             
                
            
        });
        
        
        // button on press runs the BFS algorithm
        
        
        /**
         * runs the BFS algorithm and sets the shortest path in the maze in blue
         * records the length of time the algorithm takes
         * updates the text with the shortest path length and the execution time
         */
        runBFS_Button.setOnAction((event) -> {
            Long start_time = System.nanoTime();
            bfs.runBFS(this.origin);
            
            bfs.shortestpath(this.destination);
            Long end_time = System.nanoTime();
            for(Tuple t: bfs.path){
                Circle blueCircle = new Circle(this.maze_stretch *t.y,this.maze_stretch * t.x,1);
                blueCircle.setFill(Color.BLUE);
                this.maze.getChildren().add(blueCircle);
            }
            long time_mil = (end_time-start_time)/1000000;
            int temp = bfs.distance[this.destination.x][this.destination.y];
            stats_BFS_Length.setText("Length of path: " + temp);
            stats_BFS_Time.setText("Length of BFS in ms: " + time_mil);
            
        });
        /**
         * Runs Dijkstra algorithm
         */
        
        
        runDijkstra_Button.setOnAction((event) -> {
           
            long time_mil = this.runDijkstra();
            double distance = dijk.getDistance(this.destination);
            stats_dijk_Length.setText("Length of path: " + distance);
            stats_dijk_Time.setText("Length of Dijkstra in ms: " + time_mil);
        });
        //button on press runs the A* algoithm
        
        /**
         * runs the A* algorithm
         */
        runAStar_Button.setOnAction((event) -> {
            
            long time_mil = this.runAStar();
            double distance = Astar.distance[this.destination.x][this.destination.y];
            stats_AStar_Length.setText("Length of path: " + distance);
            stats_AStar_Time.setText("Length of A* in ms: " + time_mil);
            
        });
        
        
        
        runJPS_button.setOnAction((event) ->{
            
            
            long time_mil = this.runJPS();
            stats_JPS_Length.setText("Length of path: " + jps.weight);
            stats_JPS_Time.setText("Length of JPS in ms: " + time_mil);
        });
        
        //Bottom left buttons and drop down menu
        String maze_options_list_array[] = {"one pixel corridor", "eight pixel corridor", "32 pixel corridor", "random map 40%", "random map 10%"};
        
        ComboBox maze_options = new ComboBox(FXCollections.observableArrayList(maze_options_list_array));
        
        HBox maze_options_select = new HBox();
        
        maze_select_button.setOnAction((event) -> {
            String map = maze_options.getValue().toString();
            if(map.equals("one pixel corridor")){
                this.file = new Filereader(this.m1);
            }else if(map.equals("eight pixel corridor")){
                this.file = new Filereader(this.m8);
            }else if(map.equals("32 pixel corridor")){
                this.file = new Filereader(this.m32);
            }else if(map.equals("random map 40%")){
                this.file = new Filereader(this.r40);
            }else{
                this.file = new Filereader(this.r10);
            }
            this.maze_array_read_from_file = this.file.returnArray();
            this.resetMap();
            
            
            
            
        });
        maze_reset_button.setOnAction((event) -> {
            this.resetMap();
        
        });
                
                
        maze_options_select.getChildren().addAll(maze_options,maze_select_button, maze_reset_button);
        borderpane.setBottom(maze_options_select);
        
        //adds the top left vbox nodes
        top_left_vbox.getChildren().add(Direction_Origin_pane);
        top_left_vbox.getChildren().add(Destination_coordinates_label);
        top_left_vbox.getChildren().add(error_location_label);
        top_left_vbox.getChildren().add(coordinate_hbox);
        
        top_left_vbox.getChildren().add(confirmOrigin);
        top_left_vbox.getChildren().add(new Label("Press the button below to run BFS algorithm"));
        top_left_vbox.getChildren().add(runBFS_Button);
        top_left_vbox.getChildren().add(runDijkstra_Button);
        top_left_vbox.getChildren().add(runAStar_Button);
        
        top_left_vbox.getChildren().add(runJPS_button);
        top_left_vbox.getChildren().add(stats_vbox);
        
        
        
        
        this.setAlgorithmButtonsDisabled();
        
        Stage instructionStage = new Stage();
        instructionStage.initModality(Modality.APPLICATION_MODAL);
        instructionStage.initOwner(stage);
        Scene instructionScene = new Scene(this.instructions());
        instructionStage.setScene(instructionScene);
        instructionStage.setTitle("User Instructions");
        
        
        
        borderpane.setLeft(top_left_vbox);
        Scene scene = new Scene(borderpane);
        stage.setScene(scene);
        stage.setTitle("Pathfinding");
        
        stage.show();
        instructionStage.show();
        
    }
    
    /**
     * visualizes the 2d array
     * @param mazeArray the 2d array which will be visualized
     */
    public void createMaze(int[][] mazeArray){
        
        Pane maze = new Pane();
        maze.setPrefSize(1026, 1026);
        for( int i = 0; i < mazeArray.length; i++){
            for(int j = 0; j < mazeArray[1].length; j++){
                if(mazeArray[i][j]==1){
                    maze.getChildren().add(new Circle(this.maze_stretch*j,this.maze_stretch*i,1));
                }else{
                    
                }
            }
        }
        this.maze = maze;
    }
    
    public void launchGUI(){
        launch(GUI.class);
    }
    private void setOrigin(Tuple T){
        this.origin = T;
    }
    private boolean isValidLocation(int x, int y){
        if(x<0 || x >= this.maze_array_read_from_file.length){
            return false;
        }
        if(y < 0 || y >= this.maze_array_read_from_file.length){
            return false;
        }
        if(this.maze_array_read_from_file[x][y]==1){
            return false;
        }
        return true;
    }
    private long runDijkstra(){
        long start_time = System.nanoTime();
        dijk.runDijkstra(this.origin,this.destination);

        NodeList dijk_shortestPath = dijk.getShortestPath();
        long end_time = System.nanoTime();
        while(!dijk_shortestPath.isEmpty()){
             Node n = dijk_shortestPath.remove();
             Circle redCircle = new Circle(this.maze_stretch *n.coordinates.y,this.maze_stretch * n.coordinates.x,1);
             redCircle.setFill(Color.RED);
             this.maze.getChildren().add(redCircle);
         }
         long time_mil = (end_time-start_time)/1000000;
         return time_mil;
    }
    private long runAStar(){
        long start_time = System.nanoTime();
            
        Astar.run_AStar(new Node(new Tuple(this.origin.x,this.origin.y)), new Node(new Tuple(this.destination.x,this.destination.y)));
        long end_time = System.nanoTime();
        NodeList AStar_shortestPath = Astar.getShortestPath();

        while(!AStar_shortestPath.isEmpty()){
            Node node = AStar_shortestPath.remove();
            Circle greenCircle = new Circle(this.maze_stretch * node.coordinates.y, this.maze_stretch * node.coordinates.x,1);
            greenCircle.setFill(Color.GREEN);
            this.maze.getChildren().add(greenCircle);
        }
        long time_mil = (end_time-start_time)/1000000;
        return time_mil;
    }
    private long runJPS(){
        long start_time = System.nanoTime();
        jps.run_JPS(new Node(new Tuple(this.origin.x,this.origin.y)), new Node(new Tuple(this.destination.x,this.destination.y)));
        long end_time = System.nanoTime();
        for(int i = 0; i < jps.maze.length; i++){
            for (int j = 0; j < jps.maze[0].length; j++){
                if(jps.jump_point[i][j]==1){
                    Circle purpleCircle = new Circle(this.maze_stretch* j, this.maze_stretch * i, 1);
                    purpleCircle.setFill(Color.PURPLE);
                    this.maze.getChildren().add(purpleCircle);

                }
            }
        }
        NodeList JPSshortestPath = jps.getShortestPath();
        
        while(!JPSshortestPath.isEmpty()){
            Node node = JPSshortestPath.remove();
            Circle yellowCircle = new Circle(this.maze_stretch * node.coordinates.y, this.maze_stretch * node.coordinates.x,1);
            yellowCircle.setFill(Color.YELLOW);
            this.maze.getChildren().add(yellowCircle);
            
        }

        long time_mil = (end_time-start_time)/1000000;
        return time_mil;
    }
    private void setAlgorithmButtonsDisabled(){
        runBFS_Button.setDisable(true);
        
        runDijkstra_Button.setDisable(true);
        runAStar_Button.setDisable(true);
        runJPS_button.setDisable(true);
    }
    private void setAlgorithmButtonsEnabled(){
        runBFS_Button.setDisable(false);
        runDijkstra_Button.setDisable(false);
        runAStar_Button.setDisable(false);
        runJPS_button.setDisable(false);
    }
    private void resetMap(){
        this.maze.getChildren().clear();
            for( int i = 0; i < maze_array_read_from_file.length; i++){
                for(int j = 0; j < maze_array_read_from_file[1].length; j++){
                    if(maze_array_read_from_file[i][j]==1){
                    maze.getChildren().add(new Circle(this.maze_stretch*j,this.maze_stretch*i,1));
                    }else{
                    
                    }
                }
            }
            this.origin_set_already= false;
            this.dest_set_already=false;
            this.Astar = new AStar(maze_array_read_from_file);
            this.bfs = new BFS(maze_array_read_from_file);
            this.dijk = new Dijkstra(maze_array_read_from_file);
            this.jps = new JPS(maze_array_read_from_file);
            
            this.setAlgorithmButtonsDisabled();
            this.confirmOrigin.setDisable(false);
    }
    private void initialize(){
        // stat Labels
        this.stats_title = new Label("Stats:");
        this.stats_BFS = new Label("BFS:");
        this.stats_BFS_Length = new Label("Length of path:");
        this.stats_BFS_Time = new Label("Length of BFS in ms:");
        this.stats_dijk = new Label("Dijkstra: ");
        this.stats_dijk_Length = new Label("Length of path: ");
        this.stats_dijk_Time = new Label("Length of Dijkstra in ms:");
        this.stats_AStar = new Label("A*: ");
        this.stats_AStar_Length = new Label("Length of path: ");
        this.stats_AStar_Time = new Label("Length of A* in ms: ");
        this.stats_JPS = new Label("JPS: ");
        this.stats_JPS_Length = new Label("Length of path: ");
        this.stats_JPS_Time = new Label("Length of JPS in ms: ");

        // Buttons
        this.confirmOrigin = new Button("confirm");
        this.runBFS_Button = new Button("Run BFS");
        this.runDijkstra_Button = new Button("Run Dijkstra");
        this.runAStar_Button = new Button("Run A*");
        this.runJPS_button = new Button("Run JPS");
        this.maze_select_button = new Button("select");
        this.maze_reset_button = new Button("reset maze");
    }
    
    private VBox instructions(){
        
        Label label1 = new Label("Click on the desired starting location in the maze or input manually into the text field and click the 'confirm' button to confirm your choice");
        label1.setFont(new Font(20));
        Label label2 = new Label("Repeat the process to confirm the destination");
        label2.setFont(new Font(20));
        Label label3 = new Label("Click on the button 'run BFS' to run the BFS algorithm,the algorithm will run and the shortest path will be displayed in blue");
        label3.setFont(new Font(20));
        Label label4 = new Label("Click on the button 'run Dijkstra' to run the Dijkstra algorithm, the shortest path will be displayed in red");
        label4.setFont(new Font(20));
        Label label5 = new Label("Click on the button 'run A*' to run the A Star algorithm, the shortest path will be displayed in green");
        label5.setFont(new Font(20));
        Label label6 = new Label("Click on the button 'run JPS' to run the Jump Point Search algorithm, the shortest path will be displayed in green");
        label6.setFont(new Font(20));
        Label label7 = new Label("The button 'reset' on the bottom left of the window will reset the map, sometimes it takes a while so be patient");
        label7.setFont(new Font(20));
        Label label8 = new Label("to change the map click on the drop down menu and select a map then click on the 'select' button");
        label8.setFont(new Font(20));
        VBox box = new VBox();
        box.setSpacing(10);
        box.getChildren().addAll(label1,label2,label3,label4,label5,label6,label7,label8);
        return box;
    }
    
    
    
    
}
