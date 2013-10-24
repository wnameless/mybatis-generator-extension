/**
 * 
 * @author Wei-Ming Wu
 * 
 * 
 *         Copyright 2013 Wei-Ming Wu
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 * 
 */
package wmw.db.mybatis;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

abstract public class MyBatisBase<T, E, M> implements MyBatisCRUD<T, E> {

  abstract protected SqlSessionFactory getSessionFactory();

  abstract protected Class<E> getExampleClass();

  abstract protected Class<M> getMapperClass();

  @Override
  public void insert(T record) {
    SqlSession session = null;
    try {
      session = getSessionFactory().openSession();
      M mapper = session.getMapper(getMapperClass());
      Method method =
          getMapperClass().getDeclaredMethod("insert", record.getClass());
      method.invoke(mapper, record);
      session.commit();
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
    } finally {
      if (session != null)
        session.close();
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<T> select(Example<E> example) {
    List<T> records = new ArrayList<T>();
    SqlSession session = null;
    try {
      session = getSessionFactory().openSession();
      M mapper = session.getMapper(getMapperClass());
      E ex = getExampleClass().newInstance();
      example.set(ex);
      Method method =
          getMapperClass().getDeclaredMethod("selectByExample",
              getExampleClass());
      records = (List<T>) method.invoke(mapper, ex);
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
    } finally {
      if (session != null)
        session.close();
    }
    return records;
  }

  @Override
  public List<T> selectAll() {
    return select(new Example<E>() {

      @Override
      public void set(E example) {}

    });
  }

  @Override
  public T selectOne(Example<E> example) {
    List<T> records = select(example);
    return records.isEmpty() ? null : records.get(0);
  }

  @Override
  public void update(T record, Example<E> example) {
    SqlSession session = null;
    try {
      session = getSessionFactory().openSession();
      M mapper = session.getMapper(getMapperClass());
      E ex = getExampleClass().newInstance();
      example.set(ex);
      Method method =
          getMapperClass().getDeclaredMethod("updateByExample",
              record.getClass(), getExampleClass());
      method.invoke(mapper, record, ex);
      session.commit();
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
    } finally {
      if (session != null)
        session.close();
    }
  }

  @Override
  public void delete(Example<E> example) {
    SqlSession session = null;
    try {
      session = getSessionFactory().openSession();
      M mapper = session.getMapper(getMapperClass());
      E ex = getExampleClass().newInstance();
      example.set(ex);
      Method method =
          getMapperClass().getDeclaredMethod("deleteByExample",
              getExampleClass());
      method.invoke(mapper, ex);
      session.commit();
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
    } finally {
      if (session != null)
        session.close();
    }
  }

  @Override
  public int count(Example<E> example) {
    int count = -1;
    SqlSession session = null;
    try {
      session = getSessionFactory().openSession();
      M mapper = session.getMapper(getMapperClass());
      E ex = getExampleClass().newInstance();
      example.set(ex);
      Method method =
          getMapperClass().getDeclaredMethod("countByExample",
              getExampleClass());
      count = (Integer) method.invoke(mapper, ex);
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
    } finally {
      if (session != null)
        session.close();
    }
    return count;
  }

  @Override
  public int countAll() {
    return count(new Example<E>() {

      @Override
      public void set(E example) {}

    });
  }

}
