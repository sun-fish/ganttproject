/*
Copyright 2018 BarD Software s.r.o

This file is part of GanttProject, an opensource project management tool.

GanttProject is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

GanttProject is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with GanttProject.  If not, see <http://www.gnu.org/licenses/>.
*/
package biz.ganttproject.core.option;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author dbarashev@bardsoftware.com
 */
public abstract class KeyValueOption extends GPAbstractOption<Map.Entry<String, String>> implements ListOption<Map.Entry<String, String>> {
  private static final Function<Map.Entry<String, String>, String> ENTRY_TO_KEY_VALUE = new Function<Map.Entry<String, String>, String>() {
    @Override
    public String apply(Map.Entry<String, String> entry) {
      return String.format("%s = %s", entry.getKey(), entry.getValue());
    }
  };
  private Map<String, String> myMap = Maps.newHashMap();

  public KeyValueOption(String id) {
    super(id);
  }

  @Override
  public String getPersistentValue() {
    return "\n" + Joiner.on('\n').join(Iterables.transform(getValues(), ENTRY_TO_KEY_VALUE)) + "\n";
  }

  @Override
  public void loadPersistentValue(String value) {
    myMap.clear();
    for (String line : value.split("\n")) {
      String[] keyValue = line.split("=");
      if (keyValue.length < 2) {
        continue;
      }
      myMap.put(keyValue[0].trim(), keyValue[1].trim());
    }
    fireChangeValueEvent(new ChangeValueEvent(getID(), null, null, this));
  }

  @Override
  public void setValues(Iterable<Map.Entry<String, String>> values) {
    for (Map.Entry<String, String> e : values) {
      myMap.put(e.getKey(), e.getValue());
    }
  }

  @Override
  public Iterable<Map.Entry<String, String>> getValues() {
    return myMap.entrySet();
  }

  public Map<String, String> getKeyValueMap() {
    return ImmutableMap.copyOf(myMap);
  }
}
