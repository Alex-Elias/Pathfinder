package algorithms;


import datastructures.JPS_Node;
import datastructures.Tuple;
import datastructures.PriorityQueue;


/**
 * Functions poorly and not on all maps yet
 * 
 * poor implementation of the Jump Point Search algorithm
 * @author alex
 */
public class JPS {

    public int[][] maze;
    public JPS_Node start;
    public JPS_Node goal;
    PriorityQueue queue;
    
    JPS_Node[][] predecessor;
    public int[][] jump_point;
    public double weight;
    boolean[][] visited;
    
    
    public JPS(int[][] maze){
        this.maze = maze;
        this.queue= new PriorityQueue();
        this.predecessor = new JPS_Node[maze.length][maze[0].length];
        
        this.jump_point = new int[maze.length][maze[0].length];
        this.visited = new boolean[maze.length][maze[0].length];
        
    }
    public void run_JPS( JPS_Node start, JPS_Node goal){
        start.f=0;
        start.parent=start;
        this.queue.insert(start,start.f);
        this.predecessor[start.coordinates.x][start.coordinates.y] = start;
        
//        for (int i = 0; i< this.maze.length; i++){
//            for (int j = 0; j<this.maze[0].length; j++){
//                this.distance[i][j] = 2147483647;
//                this.jump_point[i][j] = 0;
//                
//                
//            }
//            
//        }
        //this.distance[start.coordinates.x][start.coordinates.y] = 0;
        
        while(!this.queue.isEmpty()){
            
            
            JPS_Node current = this.queue.deleteMin();
            if(this.visited[current.coordinates.x][current.coordinates.y]){
               continue; 
            }
            this.visited[current.coordinates.x][current.coordinates.y] = true;
            
            
            if(current.coordinates.x == goal.coordinates.x && current.coordinates.y == goal.coordinates.y){
                this.weight = current.weight;
                this.goal = current;
                break;
            }
            JPS_Node[] next = get_successors(current,start,goal);
            for(int i = 0; i< next.length; i++){
                if(next[i]== null){
                    continue;
                }
                this.jump_point[next[i].coordinates.x][next[i].coordinates.y] = 1;
                next[i].f = next[i].weight + heuristic(next[i],goal);
                this.queue.insert(next[i],next[i].f);
            }


            
            
        }
        
        
    }
    
    public JPS_Node is_valid_place(Tuple T){
        if(T.x< 0 || T.y < 0){
            return null;
        }
        if(T.x>=this.maze.length || T.y >= this.maze[0].length){
            return null;
        }
        if(this.maze[T.x][T.y] == 1){
            return null;
        }
        return new JPS_Node(T);
    }
    public boolean is_valid(int x, int y){
        if(x< 0 || y < 0){
            return false;
        }
        if(x>=this.maze.length || y >= this.maze[0].length){
            return false;
        }
        if(this.maze[x][y] == 1){
            return false;
        }
        return true;
    }
    
    public double heuristic(JPS_Node location, JPS_Node location2){
        double dx = Math.abs(location.coordinates.x - location2.coordinates.x);
        double dy = Math.abs(location.coordinates.y - location2.coordinates.y);
        
        return dx+dy +(1.4142135 -2) * Math.min(dx, dy);
        
    }

    
    public JPS_Node[] get_successors(JPS_Node node, JPS_Node start, JPS_Node goal){
        JPS_Node[] successors = new JPS_Node[9];
        JPS_Node[] neighbors = get_neighbor_pruned(node);
        for(int i = 0; i < neighbors.length; i++){
            if(neighbors[i]== null){
                continue;
            }
            JPS_Node jump = jump(node, get_directions(neighbors[i]),start,goal);
            if(jump != null){
                //need to implement distance and heuristics here or possibly in the jump method
            }
            successors[i]= jump;
            if(jump!= null){
                this.predecessor[jump.coordinates.x][jump.coordinates.y] = node;

            }
        }
        return successors;
    }
    public JPS_Node jump(JPS_Node node, Tuple direction, JPS_Node start, JPS_Node goal){
        
        Tuple next = new Tuple(node.coordinates.x + direction.x, node.coordinates.y + direction.y);
        JPS_Node n = is_valid_place(next);
        
        if(n == null){
            return null;
        }
        n.parent = node;
        if(direction.x !=0 && direction.y !=0){
            n.weight = n.parent.weight + 1.4142135;
        }else{
            n.weight = n.parent.weight + 1;
        }
        
        if(n.coordinates.x == goal.coordinates.x && n.coordinates.y == goal.coordinates.y){
            this.goal = n;
            return n;
        }
        JPS_Node[] neighbors = get_neighbor_pruned(n);
        for(int i = 0; i< neighbors.length;i++){
            if(neighbors[i]== null){
                continue;
            }
            if(neighbors[i].forced){
                return n;
            }
        }
        if(direction.x !=0 && direction.y !=0){
            if( jump(n, new Tuple(direction.x,0),start,goal) != null || jump(n, new Tuple(0,direction.y),start,goal) != null){
                return n;
            } 
        }
        
        
        
        
        
        return jump(n,direction,start,goal);
    }
    
    
    public JPS_Node[] get_neighbors(JPS_Node node){
        JPS_Node[] neighbors = new JPS_Node[9];
        if (node.coordinates.x +1 < this.maze.length){
            if(this.maze[node.coordinates.x + 1][node.coordinates.y] ==0){
                neighbors[6] = (new JPS_Node(new Tuple(node.coordinates.x+1,node.coordinates.y)));
                neighbors[6].parent = node;
            }
        }
        if (node.coordinates.y +1 < this.maze[0].length){
            if(this.maze[node.coordinates.x][node.coordinates.y +1] == 0){
                neighbors[4] = (new JPS_Node (new Tuple(node.coordinates.x,node.coordinates.y+1)));
                neighbors[4].parent = node;
            }
        }
        if (node.coordinates.x -1 >= 0){
            if (this.maze[node.coordinates.x -1][node.coordinates.y] == 0){
                neighbors[2]=(new JPS_Node (new Tuple(node.coordinates.x-1,node.coordinates.y)));
                neighbors[2].parent = node;
            }
        }
        if (node.coordinates.y -1 >= 0){
            if (this.maze[node.coordinates.x][node.coordinates.y-1] == 0){
                neighbors[8]=(new JPS_Node (new Tuple(node.coordinates.x,node.coordinates.y-1)));
                neighbors[8].parent = node;
            }
        }
        if (node.coordinates.x +1 < this.maze.length && node.coordinates.y +1 < this.maze[0].length){
            if(this.maze[node.coordinates.x + 1][node.coordinates.y+1] ==0){
                neighbors[5]=(new JPS_Node(new Tuple(node.coordinates.x+1,node.coordinates.y+1)));
                neighbors[5].parent = node;
            }
        }
        if (node.coordinates.x -1 >=0 && node.coordinates.y +1 < this.maze[0].length){
            if(this.maze[node.coordinates.x - 1][node.coordinates.y+1] ==0){
                neighbors[3]=(new JPS_Node(new Tuple(node.coordinates.x-1,node.coordinates.y+1)));
                neighbors[3].parent = node;
            }
        }
        if (node.coordinates.x +1 < this.maze.length && node.coordinates.y -1 >=0){
            if(this.maze[node.coordinates.x + 1][node.coordinates.y-1] ==0){
                neighbors[7]=(new JPS_Node(new Tuple(node.coordinates.x+1,node.coordinates.y-1)));
                neighbors[7].parent = node;
            }
        }
        if (node.coordinates.x -1 >=0 && node.coordinates.y-1 >=0){
            if(this.maze[node.coordinates.x - 1][node.coordinates.y-1] ==0){
                neighbors[1]=(new JPS_Node(new Tuple(node.coordinates.x-1,node.coordinates.y-1)));
                neighbors[1].parent = node;
            }
        }
        return neighbors;
    }
    public JPS_Node[] get_neighbor_pruned(JPS_Node node){
        int dx = node.coordinates.x - node.parent.coordinates.x;
        int dy = node.coordinates.y - node.parent.coordinates.y;
        JPS_Node[] pruned = new JPS_Node[5];
        if(dx==0 && dy == 0){
            return get_neighbors(node);
        }
        
        if(dx == 0){
            if (is_valid(node.coordinates.x, node.coordinates.y + dy)){
                if(!is_valid(node.coordinates.x +1, node.coordinates.y)){
                    JPS_Node forced = is_valid_place(new Tuple(node.coordinates.x +1, node.coordinates.y + dy));
                    if(forced != null){
                        forced.parent = node;
                        forced.forced =true;
                        pruned[1] = forced;
                    }
                }
                if(!is_valid(node.coordinates.x -1, node.coordinates.y)){
                    JPS_Node forced = is_valid_place(new Tuple(node.coordinates.x -1, node.coordinates.y + dy));
                    if(forced != null){
                        forced.parent = node;
                        forced.forced=true;
                        pruned[2] = forced;
                    }
                }
                JPS_Node forced = is_valid_place(new Tuple(node.coordinates.x, node.coordinates.y + dy));
                forced.parent = node;
                pruned[3]= forced;
            }
        }
        if( dy==0){
            if (is_valid(node.coordinates.x + dx, node.coordinates.y)){
                if(!is_valid(node.coordinates.x, node.coordinates.y + 1)){
                    JPS_Node forced = is_valid_place(new Tuple(node.coordinates.x +dx, node.coordinates.y + 1));
                    if(forced != null){
                        forced.parent = node;
                        forced.forced =true;
                        pruned[1] = forced;
                    }
                }
                if(!is_valid(node.coordinates.x, node.coordinates.y - 1)){
                    JPS_Node forced = is_valid_place(new Tuple(node.coordinates.x +dx, node.coordinates.y -1));
                    if(forced != null){
                        forced.parent = node;
                        forced.forced=true;
                        pruned[2] = forced;
                    }
                }
                JPS_Node forced = is_valid_place(new Tuple(node.coordinates.x + dx, node.coordinates.y ));
                forced.parent = node;
                pruned[3]= forced;
            }
        }
        if(dx != 0 && dy != 0){
            if(!is_valid(node.coordinates.x, node.coordinates.y + (dy * -1))){
                JPS_Node forced = is_valid_place(new Tuple(node.coordinates.x + dx, node.coordinates.y + (dy*-1)));
                if(forced !=null){
                    forced.parent = node;
                    forced.forced=true;
                    pruned[0] = forced;
                }
            }
            if(!is_valid(node.coordinates.x + (dx*-1),node.coordinates.y)){
                JPS_Node forced = is_valid_place(new Tuple(node.coordinates.x + (dx*-1), node.coordinates.y + dy));
                if(forced != null){
                    forced.parent=node;
                    forced.forced=true;
                    pruned[1]=forced;
                }
            }
            JPS_Node temp = is_valid_place(new Tuple(node.coordinates.x+dx, node.coordinates.y+dy));
            if(temp!=null){
            temp.parent=node;
            pruned[2]=temp;
            }
            
            JPS_Node temp1 = is_valid_place(new Tuple(node.coordinates.x, node.coordinates.y+dy));
            if(temp1!=null){
            temp1.parent=node; 
            pruned[3]=temp1;
            }
            
            JPS_Node temp2 = is_valid_place(new Tuple(node.coordinates.x+dx, node.coordinates.y));
            if(temp2!=null){
            temp2.parent=node; 
            pruned[4]=temp2;
            }
            
        }
        return pruned;
        
    }
    
    
    
    
    public Tuple get_directions(JPS_Node node){
        int x = node.coordinates.x -node.parent.coordinates.x;
        int y = node.coordinates.y -node.parent.coordinates.y;
        return new Tuple(x,y);
    }
    
    


    
}