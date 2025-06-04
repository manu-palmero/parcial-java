package parcial.dao;

import java.util.ArrayList;

import parcial.model.Cliente;

public interface DAOInterface {
    abstract void inicializar();
    abstract ArrayList<Cliente> obtenerClientes();
    abstract boolean agregarCliente(Cliente cliente);
    abstract boolean eliminarCliente(int dni);
    abstract Cliente buscarCliente(int dni);
}
