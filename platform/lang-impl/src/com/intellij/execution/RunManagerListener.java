/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.execution;

import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EventListener;

public interface RunManagerListener extends EventListener {
  Topic<RunManagerListener> TOPIC = new Topic<>("RunManager", RunManagerListener.class);

  default void runConfigurationSelected() {
  }

  default void beforeRunTasksChanged() {
  }

  default void runConfigurationAdded(@NotNull RunnerAndConfigurationSettings settings) {
  }

  default void runConfigurationRemoved(@NotNull RunnerAndConfigurationSettings settings) {
  }

  default void runConfigurationChanged(@NotNull RunnerAndConfigurationSettings settings, @Nullable String existingId) {
    runConfigurationChanged(settings);
  }

  default void runConfigurationChanged(@NotNull RunnerAndConfigurationSettings settings) {
  }

  default void beginUpdate() {
  }

  default void endUpdate() {
  }
}
