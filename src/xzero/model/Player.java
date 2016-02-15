package xzero.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import xzero.model.events.PlayerActionEvent;
import xzero.model.events.PlayerActionListener;

/**
 *  Игрок, который размещает предложенную ему метку
 */
public class Player {
    private String _name;
    private Label _activeLabel;
   
    protected GameField _field;// = new GameField();
    
// --------------------------------- Имя игрока -------------------------------    
   
    public void setName(String name) {
        _name = name;
    }
    
    public String name() {
        return _name;
    }

 // ----------------------------------------------------------------------------   

    public Player (String name) {
        _name = name;
    }
 // ----------------------------------------------------------------------------   
    
    public void setGameField(GameField gameField){
        _field = gameField;
    }
    
// ---------------------- Метка, которую нужно установить -----------------------    
    
    public void setActiveLabel(Label l) {
        _activeLabel = l;
        l.setPlayer(this);
        fireLabelIsReceived(l);
    }

    public Label activeLabel() {
        return _activeLabel;
    }
    
    public void setLabelTo(Point pos){
        
        // TODO породить исключение, если не задана активная метка 
        Cell activeCell = _field.cell(pos);
        activeCell.setLabel(_activeLabel);

        fireLabelIsPlaced(_activeLabel);
        _activeLabel = null;
        
        // Генерируем событие
        
        // Повторно использовать метку нельзя
    }
    
    public List<Label> labels(){
        
        return null;
    }   
    
    // ---------------------- Порождает события -----------------------------
    
    private ArrayList _playerListenerList = new ArrayList();
    
    // Присоединяет слушателя
    public void addPlayerActionListener(PlayerActionListener l) {
        _playerListenerList.add(l);
    }
    
    // Отсоединяет слушателя
    public void removePlayerActionListener(PlayerActionListener l) {
        _playerListenerList.remove(l);
    } 
    
    // Оповещает слушателей о событии
    protected void fireLabelIsPlaced(Label l) {
        for (Object listener : _playerListenerList)
        {
            PlayerActionEvent e = new PlayerActionEvent(this);
            e.setLabel(l);
            e.setPlayer(this);            
            ((PlayerActionListener)listener).labelisPlaced(e);
        }        
    }

    protected void fireLabelIsReceived(Label l) {
        for (Object listener : _playerListenerList)
        {
            PlayerActionEvent e = new PlayerActionEvent(this);
            e.setLabel(l);
            e.setPlayer(this);            
            ((PlayerActionListener)listener).labelIsReceived(e);
        }     
    }     
}
