package xzero.model;

import java.awt.Point;

/*
 * Ячейка, которая может блокировать диагональные комбинации, является составной частью поля и содержащая в себе метку
 */

public class BlockCell extends Cell{

    
            
     @Override
     public void setLabel(Label l) 
     {
        l.setCell(this);
        _label = l;
    }  
    
    public void searchNearCells(Point pos)
     {
     }
}
