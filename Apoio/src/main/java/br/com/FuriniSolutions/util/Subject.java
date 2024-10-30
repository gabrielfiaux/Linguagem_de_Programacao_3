package br.com.FuriniSolutions.util;

import br.com.FuriniSolutions.bean.NotaFiscal;

public interface Subject {
    public void addObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyObservers(NotaFiscal notaNova);
    
}
