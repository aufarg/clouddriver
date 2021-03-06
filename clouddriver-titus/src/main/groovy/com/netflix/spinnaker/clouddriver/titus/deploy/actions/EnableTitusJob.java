/*
 * Copyright 2020 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.titus.deploy.actions;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.netflix.spinnaker.clouddriver.event.EventMetadata;
import com.netflix.spinnaker.clouddriver.saga.SagaCommand;
import com.netflix.spinnaker.clouddriver.saga.flow.SagaAction;
import com.netflix.spinnaker.clouddriver.saga.models.Saga;
import com.netflix.spinnaker.clouddriver.security.AccountCredentialsProvider;
import com.netflix.spinnaker.clouddriver.titus.TitusClientProvider;
import com.netflix.spinnaker.clouddriver.titus.deploy.description.EnableDisableServerGroupDescription;
import com.netflix.spinnaker.clouddriver.titus.deploy.ops.discovery.TitusEurekaSupport;
import javax.annotation.Nonnull;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EnableTitusJob extends AbstractTitusEnableDisableAction
    implements SagaAction<EnableTitusJob.EnableTitusJobCommand> {

  @Autowired
  public EnableTitusJob(
      AccountCredentialsProvider accountCredentialsProvider,
      TitusEurekaSupport discoverySupport,
      TitusClientProvider titusClientProvider) {
    super(accountCredentialsProvider, discoverySupport, titusClientProvider);
  }

  @NotNull
  @Override
  public Result apply(@NotNull EnableTitusJob.EnableTitusJobCommand command, @NotNull Saga saga) {
    super.markJob(saga, command.getDescription(), false);

    return new Result();
  }

  @Builder(builderClassName = "EnableTitusJobCommandBuilder", toBuilder = true)
  @JsonDeserialize(
      builder = EnableTitusJob.EnableTitusJobCommand.EnableTitusJobCommandBuilder.class)
  @JsonTypeName("enableTitusJobCommand")
  @Value
  public static class EnableTitusJobCommand implements SagaCommand {
    @Nonnull EnableDisableServerGroupDescription description;

    @NonFinal EventMetadata metadata;

    @Override
    public void setMetadata(EventMetadata metadata) {
      this.metadata = metadata;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class EnableTitusJobCommandBuilder {}
  }
}
