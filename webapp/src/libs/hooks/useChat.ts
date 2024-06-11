// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import { Constants } from '../../Constants';
import botIcon1 from '../../assets/bot-icons/bot-icon-1.png';
import botIcon2 from '../../assets/bot-icons/bot-icon-2.png';
import botIcon3 from '../../assets/bot-icons/bot-icon-3.png';
import botIcon4 from '../../assets/bot-icons/bot-icon-4.png';
import botIcon5 from '../../assets/bot-icons/bot-icon-5.png';
import { getErrorDetails } from '../../components/utils/TextUtils';
import { useAppDispatch, useAppSelector } from '../../redux/app/hooks';
import { RootState } from '../../redux/app/store';
import { BotChatUser, DefaultActiveUserInfo, DefaultChatUser, FeatureKeys } from '../../redux/features/app/AppState';
import { addAlert, toggleFeatureState } from '../../redux/features/app/appSlice';
import { ChatState } from '../../redux/features/conversations/ChatState';
import { Conversations } from '../../redux/features/conversations/ConversationsState';
import {
    addConversation,
    addMessageToConversationFromServer,
    addMessageToConversationFromUser,
    deleteConversation,
    setConversations,
    setSelectedConversation,
    updateBotResponseStatus,
    updateMessageProperty,
} from '../../redux/features/conversations/conversationsSlice';
import { AlertType } from '../models/AlertType';
import { ChatArchive } from '../models/ChatArchive';
import { AuthorRoles, ChatMessageType, IChatMessage } from '../models/ChatMessage';
import { IChatSession, ICreateChatSessionResponse } from '../models/ChatSession';
import { IChatUser } from '../models/ChatUser';
import { PlanState } from '../models/Plan';
import { IAskVariables } from '../semantic-kernel/model/Ask';
import { ContextVariable } from '../semantic-kernel/model/AskResult';
import { ChatArchiveService } from '../services/ChatArchiveService';
import { ChatService } from '../services/ChatService';
import { DocumentImportService } from '../services/DocumentImportService';
export interface GetResponseOptions {
    messageType: ChatMessageType;
    value: string;
    chatId: string;
    kernelArguments?: IAskVariables[];
    processPlan?: boolean;
}

export interface ChatStreamEvent {
    type: string;
    content: string;
}

export const useChat = () => {
    const dispatch = useAppDispatch();
    const { conversations } = useAppSelector((state: RootState) => state.conversations);
    const { activeUserInfo, features } = useAppSelector((state: RootState) => state.app);

    const botService = new ChatArchiveService();
    const chatService = new ChatService();
    const documentImportService = new DocumentImportService();

    const botProfilePictures: string[] = [botIcon1, botIcon2, botIcon3, botIcon4, botIcon5];

    const userId = activeUserInfo?.id ?? '';
    const fullName = activeUserInfo?.username ?? '';
    const emailAddress = activeUserInfo?.email ?? '';
    const loggedInUser: IChatUser = {
        id: userId,
        fullName,
        emailAddress,
        photo: undefined, // TODO: [Issue #45] Make call to Graph /me endpoint to load photo
        online: true,
        isTyping: false,
    };

    const getChatUserById = (id: string, chatId: string, users: IChatUser[]) => {
        if (id === `${chatId}-bot` || id.toLocaleLowerCase() === BotChatUser.id) return Constants.bot.profile;
        return users.find((user) => user.id === id);
    };

    const createChat = async () => {
        const chatTitle = `Copilot @ ${new Date().toLocaleString()}`;
        try {
            await chatService
                .createChatAsync(DefaultActiveUserInfo.id, chatTitle)
                .then((result: ICreateChatSessionResponse) => {
                    const newChat: ChatState = {
                        id: result.chatSession.id,
                        title: result.chatSession.title,
                        systemDescription: result.chatSession.systemDescription,
                        memoryBalance: result.chatSession.memoryBalance,
                        messages: [result.initialBotMessage],
                        enabledHostedPlugins: result.chatSession.enabledPlugins,
                        users: [loggedInUser],
                        botProfilePicture: getBotProfilePicture(Object.keys(conversations).length),
                        input: '',
                        botResponseStatus: undefined,
                        userDataLoaded: false,
                        disabled: false,
                        hidden: false,
                    };

                    dispatch(addConversation(newChat));
                    return newChat.id;
                });
        } catch (e: any) {
            const errorMessage = `Unable to create new chat. Details: ${getErrorDetails(e)}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));
        }
    };

    /*
            history: IChatMessage[],
        input: string,
        chatId: string,
        event_handler
            */
    const getResponse = async ({ messageType, value, chatId, processPlan }: GetResponseOptions) => {
        const chatInput: IChatMessage = {
            chatId: chatId,
            timestamp: new Date().getTime(),
            userId: activeUserInfo?.id as string,
            userName: activeUserInfo?.username as string,
            content: value,
            type: 0,
            authorRole: AuthorRoles.User,
        };

        const conversation = conversations[chatId];
        const history = conversation.messages;

        console.log('getResponse', chatId, chatInput);
        dispatch(addMessageToConversationFromUser({ message: chatInput, chatId: chatId }));


        let generatedText = '';
        let messageId = '111111111';
        function handle_event(event: ChatStreamEvent) {
            if (event.type === 'MessageId') {
                messageId = event.content;

                const botMessage: IChatMessage = {
                    id: messageId,
                    chatId: chatId,
                    timestamp: new Date().getTime(),
                    userId: BotChatUser.id,
                    userName: BotChatUser.fullName,
                    content: generatedText,
                    type: messageType,
                    authorRole: AuthorRoles.Bot,
                };

                dispatch(addMessageToConversationFromServer({ message: botMessage, chatId: chatId }));
            }
            else if (event.type === 'stream') {
                generatedText += event.content;

                dispatch(
                    updateMessageProperty({
                        chatId,
                        messageIdOrIndex: messageId,
                        property: 'content',
                        value: generatedText,
                        frontLoad: true,
                    }),
                );
            }
            // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
            else if (event.type === 'StatusUpdate') {
                // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
                console.log('NEw status:', event.content);
                dispatch(updateBotResponseStatus({ chatId, status: event.content }));
            } else {
                console.log('Unknown chunk:', event);
            }
        }

        try {
            await chatService.getBotResponseAsync(history, chatInput.content, chatId, handle_event);
            /*
            history: IChatMessage[],
        input: string,
        chatId: string,
        event_handler
            */
            /*const askResult = await chatService
                .getBotResponseAsyncOld(
                    ask
                )
                .catch((e: any) => {
                    throw e;
                });

            console.log('askResult', askResult);

            // Update token usage of current session

            
            const messageId = askResult.variables.find((v) => v.key === 'messageId')?.value ?? undefined;
            const botInput = askResult.variables.find((v) => v.key === 'input')?.value ?? 'None';
            
            const botMessage: IChatMessage = {
                id: messageId,
                chatId: chatId,
                timestamp: new Date().getTime(),
                userId: 'Bot',
                userName: 'Bot',
                content: botInput,
                type: messageType,
                authorRole: AuthorRoles.Bot,
            };
            dispatch(addMessageToConversationFromServer({ message: botMessage, chatId: chatId }));

            const responseTokenUsage = askResult.variables.find((v) => v.key === 'tokenUsage')?.value;
            if (responseTokenUsage) {
                const tokenUsage = JSON.parse(responseTokenUsage) as TokenUsage;
                dispatch(updateTokenUsage(tokenUsage));

                dispatch({
                    type: 'conversations/updateMessageProperty',
                    payload: {
                        chatId,
                        messageIdOrIndex: messageId,
                        property: 'tokenUsage',
                        value: tokenUsage,
                        frontLoad: true,
                    },
                });
            }
*/
            dispatch(updateBotResponseStatus({ chatId: chatId, status: undefined }));
        } catch (e: any) {
            dispatch(updateBotResponseStatus({ chatId, status: undefined }));

            const errorDetails = getErrorDetails(e);
            if (errorDetails.includes('Failed to process plan')) {
                // Error should already be reflected in bot response message. Skip alert.
                return;
            }

            const action = processPlan ? 'execute plan' : 'generate bot response';
            const errorMessage = `Unable to ${action}. Details: ${getErrorDetails(e)}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));
        }
    };

    const loadChats = async () => {
        try {
            const chatSessions = await chatService.getAllChatsAsync(DefaultActiveUserInfo.id);

            if (chatSessions.length > 0) {
                const loadedConversations: Conversations = {};
                for (const chatSession of chatSessions) {
                    const chatUsers = [DefaultChatUser];
                    const chatMessages = await chatService.getChatMessagesAsync(chatSession.id, 0, 100);

                    loadedConversations[chatSession.id] = {
                        id: chatSession.id,
                        title: chatSession.title,
                        systemDescription: chatSession.systemDescription,
                        memoryBalance: chatSession.memoryBalance,
                        users: chatUsers,
                        messages: chatMessages,
                        enabledHostedPlugins: chatSession.enabledPlugins,
                        botProfilePicture: getBotProfilePicture(Object.keys(loadedConversations).length),
                        input: '',
                        botResponseStatus: undefined,
                        userDataLoaded: false,
                        disabled: false,
                        hidden: !features[FeatureKeys.MultiUserChat].enabled && chatUsers.length > 1,
                    };
                }

                dispatch(setConversations(loadedConversations));

                // If there are no non-hidden chats, create a new chat
                const nonHiddenChats = Object.values(loadedConversations).filter((c) => !c.hidden);
                if (nonHiddenChats.length === 0) {
                    await createChat();
                } else {
                    dispatch(setSelectedConversation(nonHiddenChats[0].id));
                }
            } else {
                // No chats exist, create first chat window
                await createChat();
            }

            return true;
        } catch (e: any) {
            const errorMessage = `Unable to load chats. Details: ${getErrorDetails(e)}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));

            return false;
        }
    };

    const downloadBot = async (chatId: string) => {
        try {
            return await botService.downloadAsync(chatId);
        } catch (e: any) {
            const errorMessage = `Unable to download the bot. Details: ${getErrorDetails(e)}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));
        }

        return undefined;
    };

    const uploadBot = async (bot: ChatArchive) => {
        try {
            await botService.uploadAsync(bot).then(async (chatSession: IChatSession) => {
                const chatMessages = await chatService.getChatMessagesAsync(chatSession.id, 0, 100);

                const newChat: ChatState = {
                    id: chatSession.id,
                    title: chatSession.title,
                    systemDescription: chatSession.systemDescription,
                    memoryBalance: chatSession.memoryBalance,
                    users: [loggedInUser],
                    messages: chatMessages,
                    enabledHostedPlugins: chatSession.enabledPlugins,
                    botProfilePicture: getBotProfilePicture(Object.keys(conversations).length),
                    input: '',
                    botResponseStatus: undefined,
                    userDataLoaded: false,
                    disabled: false,
                    hidden: false,
                };

                dispatch(addConversation(newChat));
            });
        } catch (e: any) {
            const errorMessage = `Unable to upload the bot. Details: ${getErrorDetails(e)}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));
        }
    };

    const getBotProfilePicture = (index: number): string => {
        return botProfilePictures[index % botProfilePictures.length];
    };

    const getChatMemorySources = async (userId: string , chatId: string) => {
        try {
            return await chatService.getChatMemorySourcesAsync(
                userId,
                chatId,
                
            );
        } catch (e: any) {
            const errorMessage = `Unable to get chat files. Details: ${getErrorDetails(e)}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));
        }

        return [];
    };

    const getSemanticMemories = async (chatId: string, memoryName: string) => {
        try {
            return await chatService.getSemanticMemoriesAsync(
                chatId,
                memoryName,
                
            );
        } catch (e: any) {
            const errorMessage = `Unable to get semantic memories. Details: ${getErrorDetails(e)}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));
        }

        return [];
    };

    const importDocument = async (chatId: string, files: File[], uploadToGlobal: boolean) => {
        try {
            await documentImportService.importDocumentAsync(
                DefaultActiveUserInfo.id,
                chatId,
                files,
                features[FeatureKeys.AzureContentSafety].enabled,
                
                uploadToGlobal,
            );
        } catch (e: any) {
            let errorDetails = getErrorDetails(e);

            // Disable Content Safety if request was unauthorized
            const contentSafetyDisabledRegEx = /Access denied: \[Content Safety] Failed to analyze image./g;
            if (contentSafetyDisabledRegEx.test(errorDetails)) {
                if (features[FeatureKeys.AzureContentSafety].enabled) {
                    errorDetails =
                        'Unable to analyze image. Content Safety is currently disabled or unauthorized service-side. Please contact your admin to enable.';
                }

                dispatch(
                    toggleFeatureState({ feature: FeatureKeys.AzureContentSafety, deactivate: true, enable: false }),
                );
            }

            const errorMessage = `Failed to upload document(s). Details: ${errorDetails}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));
        }
    };


    const joinChat = async (chatId: string) => {
        try {
            await chatService.joinChatAsync(chatId).then(async (result: IChatSession) => {
                // Get chat messages
                const chatMessages = await chatService.getChatMessagesAsync(result.id, 0, 100);

                const newChat: ChatState = {
                    id: result.id,
                    title: result.title,
                    systemDescription: result.systemDescription,
                    memoryBalance: result.memoryBalance,
                    messages: chatMessages,
                    enabledHostedPlugins: result.enabledPlugins,
                    users: [DefaultChatUser],
                    botProfilePicture: getBotProfilePicture(Object.keys(conversations).length),
                    input: '',
                    botResponseStatus: undefined,
                    userDataLoaded: false,
                    disabled: false,
                    hidden: false,
                };

                dispatch(addConversation(newChat));
            });
        } catch (e: any) {
            const errorMessage = `Error joining chat ${chatId}. Details: ${getErrorDetails(e)}`;
            return { success: false, message: errorMessage };
        }

        return { success: true, message: '' };
    };

    const editChat = async (chatId: string, title: string, syetemDescription: string, memoryBalance: number) => {
        try {
            await chatService.editChatAsync(
                chatId,
                title,
                syetemDescription,
                memoryBalance,
                
            );
        } catch (e: any) {
            const errorMessage = `Error editing chat ${chatId}. Details: ${getErrorDetails(e)}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));
        }
    };

    const getServiceInfo = async () => {
        try {
            return await chatService.getServiceInfoAsync();
        } catch (e: any) {
            const errorMessage = `Error getting service options. Details: ${getErrorDetails(e)}`;
            dispatch(addAlert({ message: errorMessage, type: AlertType.Error }));

            return undefined;
        }
    };

    const deleteChat = async (chatId: string) => {
        const friendlyChatName = getFriendlyChatName(conversations[chatId]);
        await chatService
            .deleteChatAsync(chatId, )
            .then(() => {
                dispatch(deleteConversation(chatId));

                if (Object.values(conversations).filter((c) => !c.hidden && c.id !== chatId).length === 0) {
                    // If there are no non-hidden chats, create a new chat
                    void createChat();
                }
            })
            .catch((e: any) => {
                const errorDetails = (e as Error).message.includes('Failed to delete resources for chat id')
                    ? "Some or all resources associated with this chat couldn't be deleted. Please try again."
                    : `Details: ${(e as Error).message}`;
                dispatch(
                    addAlert({
                        message: `Unable to delete chat {${friendlyChatName}}. ${errorDetails}`,
                        type: AlertType.Error,
                        onRetry: () => void deleteChat(chatId),
                    }),
                );
            });
    };

    const processPlan = async (chatId: string, planState: PlanState, serializedPlan: string, planGoal?: string) => {
        const kernelArguments: ContextVariable[] = [
            {
                key: 'proposedPlan',
                value: serializedPlan,
            },
        ];

        let message = 'Run plan' + (planGoal ? ` with goal of: ${planGoal}` : '');
        switch (planState) {
            case PlanState.Rejected:
                message = 'No, cancel';
                break;
            case PlanState.Approved:
                message = 'Yes, proceed';
                break;
        }

        // Send plan back for processing or execution
        await getResponse({
            value: message,
            kernelArguments,
            messageType: ChatMessageType.Message,
            chatId: chatId,
            processPlan: true,
        });
    };

    return {
        getChatUserById,
        createChat,
        loadChats,
        getResponse,
        downloadBot,
        uploadBot,
        getChatMemorySources,
        getSemanticMemories,
        importDocument,
        joinChat,
        editChat,
        getServiceInfo,
        deleteChat,
        processPlan,
    };
};

export function getFriendlyChatName(convo: ChatState): string {
    const messages = convo.messages;

    // Regex to match the Copilot timestamp format that is used as the default chat name.
    // The format is: 'Copilot @ MM/DD/YYYY, hh:mm:ss AM/PM'.
    const autoGeneratedTitleRegex =
        /Copilot @ [0-9]{1,2}\/[0-9]{1,2}\/[0-9]{1,4}, [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2} [A,P]M/;
    const firstUserMessage = messages.find(
        (message) => message.authorRole !== AuthorRoles.Bot && message.type === ChatMessageType.Message,
    );

    // If the chat title is the default Copilot timestamp, use the first user message as the title.
    // If no user messages exist, use 'New Chat' as the title.
    const friendlyTitle = autoGeneratedTitleRegex.test(convo.title)
        ? firstUserMessage?.content ?? 'New Chat'
        : convo.title;

    // Truncate the title if it is too long
    return friendlyTitle.length > 60 ? friendlyTitle.substring(0, 60) + '...' : friendlyTitle;
}
