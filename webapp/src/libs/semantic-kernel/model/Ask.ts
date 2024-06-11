// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

export interface IAsk {
    input: string;
    variables?: IAskVariables[];
}

export interface IAskVariables {
    key: string;
    value: string;
}
