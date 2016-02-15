package xzero.model;

import java.util.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import xzero.model.navigation.Direction;
import xzero.model.navigation.Shift;

/**
 *  Прямоугольное поле, состоящее из ячеек
 */
public class GameField {
    
    protected int size;
    protected ArrayList<Cell> _listCells = new ArrayList<>();
    
    
// ----------------- Контейнер ячеек, а в конечном счете, и меток ---------------
// Позиции ячеек и меток  задаются строками и столбцами в диапазоне [1, height] и
// [1, width] соответсвенно
    
// ------------------------------ Ячейки ---------------------------------------    
    
    Cell cell(Point pos){
        for (Cell c : _listCells){
            if (c.position().equals(pos)) return c; 
        }
        return null;
    }
    
    void setCell(Point pos, Cell cell){
        cell.setPosition(pos);
        _listCells.add(cell);
    }
    
    public void clear(){
        _listCells.clear();
    }    
    
    private void removeCell(Point pos){
        Cell c = cell(pos);
        if (c != null) _listCells.remove(c);
    }
    
    void ChangeRandomListCell(){
        
        for (Cell c: _listCells){
            c.setLabel(null);
        }
    }
    
// ------------------------------ Метки ---------------------------------------    
    
    public Label label(Point pos){
        for (Cell c : _listCells){
            if (c.position().equals(pos)) return c.label();
        }
        return null;
    }
    
    

    public void setLabel(Point pos, Label activeLabel){
        Cell c = cell(pos);
        Point cellPos = c.position();
        if (cellPos.equals(pos)){
            c.setLabel(activeLabel);
        }
    }

    public List<Label> labels(){
        ArrayList<Label> listLabel = new ArrayList<>();
        for (Cell c : _listCells){
            listLabel.add(c.label());
        }
        return listLabel;
    }
    
    // Возвращает ряд меток, принадлежащих одному игроку
    public List<Label> labelLine(Point start, Direction direct){
        
        ArrayList<Label> line = new ArrayList<>();
        boolean isLineFinished = false;
        Player startPlayer = null;
        
        // Определяем первую метку и соответствующего ей игрока
        Point pos = new Point(start);
        Label l = label(pos);
        
        isLineFinished = (l == null);
        if(!isLineFinished)
        {
            line.add(l);
            startPlayer = line.get(0).player();
        }

        Shift shift = direct.shift();
        pos.translate(shift.byHorizontal(), shift.byVertical());
        while(!isLineFinished && containsRange(pos))
        {
            l = label(pos);
            isLineFinished = (l == null || !l.player().equals(startPlayer));
            
            if(!isLineFinished)
            {
                line.add(l); 
            }

            pos.translate(shift.byHorizontal(), shift.byVertical());
        }
        
        return line;
    }    
   
// ----------------------- Ширина и высота поля ------------------------------
    private int _width;
    private int _height;

    public void setSize(int width, int height) {
 
        _width = width;
        _height = height;
        
    }

    public int width() {
        return _width;
    }

    public int height() {
        return _height;
    }
    
    public boolean containsRange(Point p){
        return p.getX() >= 1 && p.getX() <= _width &&
               p.getY() >= 1 && p.getY() <= _height ;
    }
    
// ----------------------------------------------------------------------------    
    public GameField() {
        
        setSize(3, 3);
    }
}
