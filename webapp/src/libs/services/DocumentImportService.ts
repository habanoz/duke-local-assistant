// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import { ServiceInfo } from '../models/ServiceInfo';
import { BaseService } from './BaseService';
import { Constants } from '../../Constants';

export class DocumentImportService extends BaseService {
    public importDocumentAsync = async (
        userId: string,
        chatId: string,
        documents: File[],
        useContentSafety: boolean,
        uploadToGlobal: boolean,
    ) => {
        const formData = new FormData();
        formData.append('useContentSafety', useContentSafety.toString());
        for (const document of documents) {
            formData.append('formFiles', document);
        }
        
        if (uploadToGlobal){
            chatId  = Constants.emptyGuid;
        }

        return await this.getResponseAsync<object>(
            {
                commandPath: `${userId}/chats/${chatId}/documents`,
                method: 'POST',
                body: formData,
            }
        );
    };

    public getContentSafetyStatusAsync = async (): Promise<boolean> => {
        const serviceInfo = await this.getResponseAsync<ServiceInfo>(
            {
                commandPath: 'info',
                method: 'GET',
            },
        );

        return serviceInfo.isContentSafetyEnabled;
    };
}
