/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rba.jaxrs.autoconfig.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author AUtsch - Adam Utsch - adam.utsch@rbaconsulting.com
 * @since 12/19/2018
 */
@ConfigurationProperties(prefix = "jaxrs.autoconfig")
public class AutoConfigProperties {

    private List<String> packagesToBlacklist = new ArrayList<>();
    private List<String> packagesToScan = new ArrayList<>();

    public List<String> getPackagesToBlacklist() {
        return Collections.unmodifiableList(packagesToBlacklist);
    }

    public void setPackagesToBlacklist(List<String> packagesToBlacklist) {
        this.packagesToBlacklist = packagesToBlacklist;
    }

    public List<String> getPackagesToScan() {
        return Collections.unmodifiableList(packagesToScan);
    }

    public void setPackagesToScan(List<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }
}
