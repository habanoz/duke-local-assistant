// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import { IChatMessage } from './ChatMessage';

export interface IChatSession {
    id: string;
    title: string;
    systemDescription: string;
    memoryBalance: number;
    enabledPlugins: string[];
}

export interface ICreateChatSessionResponse {
    chatSession: IChatSession;
    initialBotMessage: IChatMessage;
}
