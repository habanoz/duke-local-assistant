// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

export interface MemoryStore {
    types: string[];
    selectedType: string;
}

export interface HostedPlugin {
    name: string;
    manifestDomain: string;
}

export interface ServiceInfo {
    memoryStore: MemoryStore;
    availablePlugins: HostedPlugin[];
    version: string;
    isContentSafetyEnabled: boolean;
}
