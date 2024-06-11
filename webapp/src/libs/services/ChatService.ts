// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import { DefaultActiveUserInfo } from '../../redux/features/app/AppState';
import { ChatStreamEvent } from '../hooks/useChat';
import { ChatMemorySource } from '../models/ChatMemorySource';
import { AuthorRoles, IChatMessage, SimpleChatMessage, ChatResponseRequest } from '../models/ChatMessage';
import { IChatSession, ICreateChatSessionResponse } from '../models/ChatSession';
import { ServiceInfo } from '../models/ServiceInfo';
import { IAsk} from '../semantic-kernel/model/Ask';
import { IAskResult } from '../semantic-kernel/model/AskResult';
import { BaseService } from './BaseService';

export class ChatService extends BaseService {
    public createChatAsync = async (userId: string, title: string): Promise<ICreateChatSessionResponse> => {
        const body = {
            title,
        };

        const result = await this.getResponseAsync<ICreateChatSessionResponse>(
            {
                commandPath: `${userId}/chats`,
                method: 'POST',
                body,
            },

        );

        return result;
    };

    public getChatAsync = async (chatId: string): Promise<IChatSession> => {
        const result = await this.getResponseAsync<IChatSession>(
            {
                commandPath: `chats/${chatId}`,
                method: 'GET',
            },

        );

        return result;
    };

    public getAllChatsAsync = async (userId:string): Promise<IChatSession[]> => {
        const result = await this.getResponseAsync<IChatSession[]>(
            {
                commandPath: `${userId}/chats`,
                method: 'GET',
            },

        );
        return result;
    };

    public getChatMessagesAsync = async (
        chatId: string,
        page: number,
        size: number,
       
    ): Promise<IChatMessage[]> => {
        const result = await this.getResponseAsync<IChatMessage[]>(
            {
                commandPath: `chats/${chatId}/messages?page=${page}&size=${size}`,
                method: 'GET',
            },

        );

        // Messages are returned with most recent message at index 0 and oldest message at the last index,
        // so we need to reverse the order for render
        return result;
    };

    public editChatAsync = async (
        chatId: string,
        title: string,
        systemDescription: string,
        memoryBalance: number,
       
    ): Promise<any> => {
        const body: IChatSession = {
            id: chatId,
            title,
            systemDescription,
            memoryBalance,
            enabledPlugins: [], // edit will not modify the enabled plugins
        };

        const result = await this.getResponseAsync<IChatSession>(
            {
                commandPath: `chats/${chatId}`,
                method: 'PATCH',
                body,
            },

        );

        return result;
    };

    public deleteChatAsync = async (chatId: string): Promise<object> => {
        const result = await this.getResponseAsync<object>(
            {
                commandPath: `chats/${chatId}`,
                method: 'DELETE',
            },

        );

        return result;
    };

    public getBotResponseAsyncOld = async (
        ask: IAsk,
    ): Promise<IAskResult> => {
        // If function requires any additional api properties, append to context
        

        const chatId = ask.variables?.find((variable) => variable.key === 'chatId')?.value as string;

        const result = await this.getResponseAsync<IAskResult>(
            {
                commandPath: `chats/${chatId}/messages`,
                method: 'POST',
                body: ask,
            }
        );
        console.log('getBotResponseAsyncRequest response:', result);
        return result;
    };

    public getBotResponseAsync = async (
        history: IChatMessage[],
        input: string,
        chatId: string,
        event_handler: (event: ChatStreamEvent)=>void
    ) => {
        const simpleChatMessages: SimpleChatMessage[] = history.map((message) => ({
            content: message.content,
            role: message.authorRole==AuthorRoles.User?'user':'assistant',
          }));

          
        simpleChatMessages.push ({'content':input, 'role':'user'});

        const chatResponseRequest: ChatResponseRequest = {
            messages: simpleChatMessages,
            userName: DefaultActiveUserInfo.username
        };

        const userId = DefaultActiveUserInfo.id;

        await this.handleResponseEventStream(
            {
                commandPath: `${userId}/chats/${chatId}/messages`,
                method: 'POST',
                body: chatResponseRequest,
            },
            event_handler = event_handler
        );
    };

    public getBotHandleEvents = async (ask: IAsk, event_handler: (event: ChatStreamEvent) => void) => {
        // If function requires any additional api properties, append to context

        await this.handleResponseEventStream(
            {
                commandPath: `chat-stream`,
                method: 'POST',
                body: ask,
            },
            event_handler = event_handler,
        );
    };

    public joinChatAsync = async (chatId: string): Promise<IChatSession> => {
        return await this.getChatAsync(chatId);
    };

    public getChatMemorySourcesAsync = async (userId: string, chatId: string): Promise<ChatMemorySource[]> => {
        const result = await this.getResponseAsync<ChatMemorySource[]>(
            {
                commandPath: `${userId}/chats/${chatId}/documents`,
                method: 'GET',
            },

        );

        return result;
    };

    public getSemanticMemoriesAsync = async (
        chatId: string,
        memoryName: string,
       
    ): Promise<string[]> => {
        const result = await this.getResponseAsync<string[]>(
            {
                commandPath: `chats/${chatId}/memories?type=${memoryName}`,
                method: 'GET',
            },

        );

        return result;
    };

    public getServiceInfoAsync = async (): Promise<ServiceInfo> => {
        const result = await this.getResponseAsync<ServiceInfo>(
            {
                commandPath: `info`,
                method: 'GET',
            },

        );

        return result;
    };
}
