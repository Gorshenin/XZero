package xzero.model;

/**
 * Метка, которую можно поместить на поле
 */
public class Label {
    
    // Игрок
    private Player _player;
    private Cell _cell;
    
// --------------- Ячейка, которой прнадлежит метка. Задает сама ячейка -------
    
    void setCell(Cell c){
        _cell = c;
    }
    
    public Cell cell(){
        return _cell;
    }
    
// - Игрок, которому прнадлежит метка. Метка может быть нейтральной (не принадлежать никому) -
    
    void setPlayer(Player p){
        _player = p;
    }

    void unsetPlayer(){
        _player = null;
    }
    
    public Player player(){
        return _player;
    } 
}
