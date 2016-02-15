package xzero.model;

import java.awt.Point;

/*
 * Ячейка, являющаяся составной частью поля и содержащая в себе метку
 */
public class Cell {
    
    // позиция
    protected Point _position;
    protected Label _label;
    
// --------------------- Позиция метки -----------------------
    
    void setPosition(Point pos){
        _position = pos;
    }
    
    public Point position(){
        return _position;
    }
    
// --------------------- Метка, принадлежащая ячейке -----------------------
    
    public Label label() {
        return _label;
    }
    
    public boolean isEmpty(){
        return _label.player() == null;
    }
    
    public void setLabel(Label l) {
        l.setCell(this);
        _label = l;
    }   

}
