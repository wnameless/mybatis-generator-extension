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

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * 
 * MyBatisGeneratorRunner generates Mybatis models by given configuration xml
 * file.
 * 
 */
public final class MyBatisGeneratorRunner {

  private MyBatisGeneratorRunner() {}

  /**
   * Generates MyBatis models.
   * 
   * @param configFile
   *          an InputStream of configuration xml file
   * @param overwrite
   *          true if generated files are over-writable, false otherwise
   */
  public static void run(InputStream configFile, boolean overwrite)
      throws IOException, XMLParserException, InvalidConfigurationException,
      SQLException, InterruptedException {
    List<String> warnings = new ArrayList<String>();
    ConfigurationParser cp = new ConfigurationParser(warnings);
    MyBatisGenerator generator =
        new MyBatisGenerator(cp.parseConfiguration(configFile),
            new DefaultShellCallback(overwrite), warnings);
    generator.generate(null);
    for (String warning : warnings) {
      Logger.getLogger(MyBatisGeneratorRunner.class.getName()).log(Level.INFO,
          null, warning);
    }
  }

}
