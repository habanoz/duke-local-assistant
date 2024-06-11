// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import { IChatMessage } from '../../models/ChatMessage';

export interface IAskResult {
    message: IChatMessage;
    variables: ContextVariable[];
}

export interface ContextVariable {
    key: string;
    value: string;
}
