// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

export interface ChatMemorySource {
    id: string;
    chatId: string;
    sourceType: string;
    name: string;
    hyperlink?: string;
    sharedBy: string;
    createdOn: number;
    size: number;
}
