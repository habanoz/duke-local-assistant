// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import { ChatArchive } from '../models/ChatArchive';
import { IChatSession } from '../models/ChatSession';
import { BaseService } from './BaseService';

export class ChatArchiveService extends BaseService {
    public downloadAsync = async (chatId: string) => {
        // TODO: [Issue #47] Add type for result. See Bot.cs
        const result = await this.getResponseAsync<object>(
            {
                commandPath: `chats/${chatId}/archive`,
                method: 'GET',
            }
        );

        return result;
    };

    public uploadAsync = async (chatArchive: ChatArchive) => {
        const result = await this.getResponseAsync<IChatSession>(
            {
                commandPath: 'chats/archives',
                method: 'POST',
                body: chatArchive,
            }
        );

        return result;
    };
}
