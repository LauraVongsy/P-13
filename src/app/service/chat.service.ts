import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom, from, Observable } from 'rxjs';
import { Client, Conversation, Message } from '@twilio/conversations';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private client?: Client;
  private conversation?: Conversation;
  public currentUser: string = '';

  constructor(private http: HttpClient) {}

  async initClient(identity: string): Promise<void> {
   const token = await firstValueFrom(
        this.http.get(`http://localhost:8080/api/chat/token/${identity}`, { responseType: 'text' })
    );
    this.currentUser = identity
    this.client = new Client(token);

    await new Promise<void>((resolve, reject) => {
        this.client!.once('initialized', () => resolve());
        this.client!.once('connectionError', (err) => reject(err));
    });

    console.log('Twilio client initialized');
  }

  async getOrCreateConversation(uniqueName: string): Promise<{conversation: Conversation, messages: { author: string; body: string }[]}> {
    if (!this.client) throw new Error('Client not initialized');
    let messages: { author: string; body: string }[] = [];

    try {
      this.conversation = await this.client.getConversationByUniqueName(uniqueName);

    messages = await this.getPreviousMessages()
    } catch {
      this.conversation = await this.client.createConversation({ uniqueName });
      // Default users added only when conversation is initially created
      this.addDefaultUsersToConversation()
    }
    return {conversation : this.conversation, messages}
  }

  async getPreviousMessages(): Promise<{ author: string; body: string }[]> {
    if(!this.conversation) return [];

       const messagePaginator = await this.conversation.getMessages()
      return messagePaginator.items.map((message) => ({
        author: message.author || 'unknown',
        body: message.body as string
      }))
  }

  async addDefaultUsersToConversation() {
    const DEFAULT_USERS = ['user1', 'user2'];
    DEFAULT_USERS.map((user) => this.conversation?.add(user));
  }

  async disconnect(): Promise<void> {
    if(this.client) {
      this.client.shutdown()
      this.client = undefined
      this.conversation = undefined
    }
  }

  async sendMessage(body: string): Promise<void> {
    if (this.conversation && body.trim()) {
      await this.conversation.sendMessage(body);
    }
  }

  onMessageAdded(callback: (msg: { author: string; body: string }) => void) {
    if (!this.conversation) return;

    this.conversation.removeAllListeners('messageAdded');

    this.conversation.on('messageAdded', (msg: Message) => {
      callback({ author: msg.author || 'unknown', body: msg.body as string });
    });
  }
}
