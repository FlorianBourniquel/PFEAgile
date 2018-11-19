package fr.unice.polytech;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;




public class Db implements AutoCloseable
{
    private final Driver driver;

    public Db()
    {
        driver = GraphDatabase.driver( "bolt://neodb:7687" , AuthTokens.basic( "neo4j", "password" ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void executeQuery(final String query )
    {
        try ( Session session = driver.session() )
        {
            session.writeTransaction(tx -> tx.run(query));
        }
    }

    public Driver getDriver() {
        return driver;
    }
}
