package xzero.model.factory;

import xzero.model.Cell;
import xzero.model.BlockCell;

/**
 * Фабрика, порождающая возможные виды ячеек. Реализует самую простую стратегию
 */
public class CellFactory {
    
    public Cell createCell(){
       return new Cell(); 
    }
    public Cell createBlockCell(){
       return new BlockCell(); 
    }
}
