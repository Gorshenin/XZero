package xzero.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import xzero.model.events.GameEvent;
import xzero.model.events.GameListener;
import xzero.model.events.PlayerActionEvent;
import xzero.model.events.PlayerActionListener;
import xzero.model.factory.CellFactory;
import xzero.model.factory.LabelFactory;
import xzero.model.navigation.Direction;

/**
/* Aбстракция всей игры; генерирует стартовую обстановку; поочередно передает 
* ход игрокам, задавая им метку для установки на поле; следит за игроками с 
* целью определения конца игры
*/
public class GameModel {
    private GameField _field;
    private Player _activePlayer;
    private Player _otherPlayer;
// -------------------------------- Поле -------------------------------------
    
    public GameField field(){
        return _field;
    }
    
// -------------------------------- Игроки -----------------------------------
    
    public Player activePlayer(){
        return _activePlayer;
    }
    
    public GameModel(){
       
        _field = new GameField();
        // Задаем размеры поля по умолчанию
        field().setSize(3, 3);
        
        // Создаем двух игроков
        Player p;
        PlayerObserver observer = new PlayerObserver();
        
        p = new Player("X");
        p.setGameField(_field);
        p.addPlayerActionListener(observer);
        _otherPlayer = p;
        
        p = new Player("O");
        p.setGameField(_field);
        p.addPlayerActionListener(observer);
        _activePlayer = p;
    }
    
// ---------------------- Порождение обстановки на поле ---------------------
    
    private CellFactory _cellFactory = new CellFactory();
    
    private void generateField(){
        field().clear();
        field().setSize(3, 3);
        for(int row = 1; row <= field().height(); row++)
        {
            for(int col = 1; col <= field().width(); col++)
            {
                if((col+row)/2>2)
                {
                field().setCell(new Point(col, row), _cellFactory.createBlockCell()); 
                }
                else
                    field().setCell(new Point(col, row), _cellFactory.createCell()); 
            }
        }
        
    }

// ----------------------------- Игровой процесс ----------------------------
    
    public void start(){
        
        // Генерируем поле
        generateField();
        
        // Передаем ход первому игроку
        exchangePlayer();
    }
    
    // мод: передача хода другому игроку
    public void skip(){

        // Передаем ход новому игроку
        exchangePlayer();
    }


    private LabelFactory _labelFactory = new LabelFactory();

    private void exchangePlayer(){

        // Сменить игрока
        Player temp = _otherPlayer;
        _otherPlayer = _activePlayer;
        _activePlayer = temp;
        
        // Выбрать для него метку
        Label newLabel = _labelFactory.createLabel();
        activePlayer().setActiveLabel(newLabel);
        // Генерируем событие
        
        PlayerActionEvent e = new PlayerActionEvent(activePlayer());
        e.setPlayer(activePlayer());
        e.setLabel(newLabel);
        
       // fireLabelIsRecived(e);
        firePlayerExchanged(activePlayer()); 
    }
    
    
    private static int WINNER_LINE_LENGTH = 3;
    
    private Player determineWinner(){
    
        for(int row = 1; row <= field().height(); row++)
        {
            for(int col = 1; col <= field().width(); col++)
            {
                Point pos = new Point(col, row);
                Direction direct = Direction.north();
                for(int  i = 1; i <= 8; i++)
                {
                    direct = direct.rightword();

                    List<Label> line = field().labelLine(pos, direct);

                    if(line.size() >= WINNER_LINE_LENGTH)
                    {
                        return line.get(0).player();
                    }
                }
            }
        }
        
        return null;
    }
   
    private class PlayerObserver implements PlayerActionListener{

        @Override
        public void labelisPlaced(PlayerActionEvent e) {
            
            //  Транслируем событие дальше для активного игрока
            if(e.player() == activePlayer()){
                fireLabelIsPlaced(e);
            }
            
            // Определяем победителя
            Player winner = determineWinner();
            
            // Меняем игрока, если игра продолжается
            if(winner == null)
            {
                exchangePlayer();
            }
            else
            { 
                // Генерируем событие
                fireGameFinished(winner);
            }
        }

        @Override
        public void labelIsReceived(PlayerActionEvent e) {
            //  Транслируем событие дальше для активного игрока
            if(e.player() == activePlayer()){
                fireLabelIsRecived(e);
            }
        }
    }
    
// ------------------------ Порождает события игры ----------------------------
    
    // Список слушателей
    private ArrayList _listenerList = new ArrayList(); 
 
    // Присоединяет слушателя
    public void addGameListener(GameListener l) { 
        _listenerList.add(l); 
    }
    
    // Отсоединяет слушателя
    public void removeGameListener(GameListener l) { 
        _listenerList.remove(l); 
    } 
    
    // Оповещает слушателей о событии
    protected void fireGameFinished(Player winner) {
        
        GameEvent event = new GameEvent(this);
        event.setPlayer(winner);
        for (Object listner : _listenerList)
        {
            ((GameListener)listner).gameFinished(event);
        }
    }     

    protected void firePlayerExchanged(Player p) {
        
        GameEvent event = new GameEvent(this);
        event.setPlayer(p);
        for (Object listner : _listenerList)
        {
            ((GameListener)listner).playerExchanged(event);
        }
    }     
    
// --------------------- Порождает события, связанные с игроками -------------
    
    // Список слушателей
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
    protected void fireLabelIsPlaced(PlayerActionEvent e) {
        
        for (Object listner : _playerListenerList)
        {
            ((PlayerActionListener)listner).labelisPlaced(e);
        }
    }     
    
    protected void fireLabelIsRecived(PlayerActionEvent e) {
        
        for (Object listner : _playerListenerList)
        {
            ((PlayerActionListener)listner).labelIsReceived(e);
        }
    }
}
