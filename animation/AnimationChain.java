package document_object.animation;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import document_object.animation.Lambda;
import document_object.animation.AnimationStep;

public class AnimationChain implements Runnable {
	
	protected ArrayList<AnimationStep> stack;
	protected Timer timer = null;
	protected AnimationChain child = null;
	protected float fps = (float)60.0;
	protected Runnable finalizer;
	
	/* easing constants */
	public static final int EASE_LINEAR = 0;
	public static final int EASE_IN_QUAD = 1;
	public static final int EASE_OUT_QUAD = 2;
	public static final int EASE_IN_OUT_QUAD = 3;
	public static final int EASE_IN_CUBIC = 4;
	public static final int EASE_OUT_CUBIC = 5;
	public static final int EASE_IN_OUT_CUBIC = 6;
	public static final int EASE_IN_QUART = 7;
	public static final int EASE_OUT_QUART = 8;
	public static final int EASE_IN_OUT_QUART = 9;
	public static final int EASE_IN_QUINT = 10;
	public static final int EASE_OUT_QUINT = 11;
	public static final int EASE_IN_OUT_QUINT = 12;
	public static final int EASE_IN_SINE = 13;
	public static final int EASE_OUT_SINE = 14;
	public static final int EASE_IN_OUT_SINE = 15;
	public static final int EASE_IN_EXPO = 16;
	public static final int EASE_OUT_EXPO = 17;
	public static final int EASE_IN_OUT_EXPO = 18;
	public static final int EASE_IN_CIRC = 19;
	public static final int EASE_OUT_CIRC = 20;
	public static final int EASE_IN_OUT_CIRC = 21;
	public static final int EASE_IN_ELASTIC = 22;
	public static final int EASE_OUT_ELASTIC = 23;
	public static final int EASE_IN_OUT_ELASTIC = 24;
	public static final int EASE_IN_BACK = 25;
	public static final int EASE_OUT_BACK = 26;
	public static final int EASE_IN_OUT_BACK = 27;
	public static final int EASE_IN_BOUNCE = 28;
	public static final int EASE_OUT_BOUNCE = 29;
	public static final int EASE_IN_OUT_BOUNCE = 30;
	
	public static final float STEPS_COUNT = (float) 1000.0;
	
	public AnimationChain() {
		this.stack = new ArrayList<AnimationStep>();
	}
	
	public AnimationChain( float fps ) {
		this.stack = new ArrayList<AnimationStep>();
		this.fps = fps;
	}
	
	public AnimationChain queue( float start, float end, int duration, Lambda<Float> handler ) {
		return this.queue( start, end, duration, EASE_LINEAR, handler );
	}
	
	public AnimationChain queue( float start, float end, int duration, int mode, Lambda<Float> handler ) {
		this.stack.add( new AnimationStep( start, end, duration, mode, handler ) );
		return this;
	}
	
	public AnimationChain then( float start, float end, int duration, Lambda<Float> handler ) {
		return this.then(start, end, duration, EASE_LINEAR, handler);
	}
	
	public AnimationChain then( float start, float end, int duration, int mode, Lambda<Float> handler ) {
		if ( this.child == null ) {
			this.child = new AnimationChain();
			child.queue( start, end, duration, mode, handler );
		} else {
			child.then(start, end, duration, mode, handler);
		}
		return this;
	}
	
	public AnimationChain ensure( Runnable handler ) {
		if ( this.child == null ) {
			this.finalizer = handler;
		} else {
			child.ensure( handler );
		}
		
		return this;
	}
	
	public void run() {
		
		int outstanding = 0;
		int fps_step = Math.round( STEPS_COUNT / fps );
		
		if ( this.timer == null ) {
			this.timer = new Timer();
		}
		
		for ( int i = 0; i < this.stack.size(); ++i ) {
			
			final AnimationStep animation_step = this.stack.get( i );
			
			if ( animation_step.getStart() == animation_step.getEnd() ) {
				continue;
			}
			
			float duration = (float)animation_step.getDuration();
			float step = (float) ( 1000 / fps );
			float current = 0;
			
			while ( current <= duration ) {
				final float val = ease( animation_step.getMode(), current,
						animation_step.getStart(), animation_step.getEnd() - animation_step.getStart(), duration );
				this.timer.schedule( new TimerTask() { public void run() {
					( (Lambda<Float> )animation_step.getHandler() ).run( val );
				} }, (long) ( current ) );
				current += step;
			}
			
			this.timer.schedule( new TimerTask() { public void run() {
				( (Lambda<Float>)animation_step.getHandler() ).run( animation_step.getEnd() );
			} }, (long) ( current ) );
			current += 1;
			
			if ( this.finalizer != null ) {
				this.timer.schedule( new TimerTask() {
					public void run() {
						finalizer.run();
					}
				}, (long) ( current ) );
				current += 1;
			}
			
			if ( this.child != null ) {
				this.timer.schedule( new TimerTask() {
					public void run() {
						child.run();
					}
				}, (long) current );
			}
		}
		
	}
	
	public void cancel() {
		if ( this.timer != null )
			this.timer.cancel();
		this.stack.clear();
		this.timer = new Timer();
	}
	
	protected float ease( int mode, float t, float b, float c, float d ) {
		float res = 0;
		float s, p, a;
		switch ( mode ) {
		case EASE_IN_QUAD:
			res = (float)( c * Math.pow( t / d, 2 ) + b );
			break;
		case EASE_OUT_QUAD:
			res = (float)( -1 * c * ( t / d ) / ( t / d - 2 ) + b );
			break;
		case EASE_IN_OUT_QUAD:
			if ( t < d/2 ) {
				res = (float) (c/2 * Math.pow( t / (d/2), 2 ) + b);
			} else {
				res = -c / 2 * ( ( t / (d/2) - 1 ) * ( t / (d/2) - 3 ) - 1 )  + b;
			}
			break;
		case EASE_IN_CUBIC:
			res = (float) (c * Math.pow( t / d, 3 ) + b);
			break;
		case EASE_OUT_CUBIC:
			res = (float) (c * ( Math.pow( t / d - 1, 3 ) + 1 ) + b);
			break;
		case EASE_IN_OUT_CUBIC:
			if ( t < d/2 ) {
				res = (float) ((c/2) * Math.pow( t / (d/2), 3 ) + b);
			} else {
				res = (float) ((c/2) * ( Math.pow( t / (d/2) - 2, 3 ) + 2 ) + b);
			}
			break;
		case EASE_IN_QUART:
			res = (float) (c * Math.pow( t / d, 4 ) + b);
			break;
		case EASE_OUT_QUART:
			res = (float) ( -c * ( Math.pow( t / d - 1, 4 ) - 1 ) + b);
			break;
		case EASE_IN_OUT_QUART:
			if ( t < d/2 ) {
				res = (float) ( c/2  * Math.pow( t / (d/2), 4 ) ) + b;
			} else {
				res = (float) ( -c/2 * ( Math.pow( t / (d/2) - 2, 4 ) - 2 ) ) + b;
			}
			break;
		case EASE_IN_QUINT:
			res = (float) ( c * Math.pow( t / d, 5 ) ) + b;
			break;
		case EASE_OUT_QUINT:
			res = (float) (c * ( Math.pow( t/d - 1, 5 ) + 1 ) + b );
			break;
		case EASE_IN_OUT_QUINT:
			if ( t < d/2 ) {
				res = (float) ( c/2 * Math.pow( t / (d/2), 5 ) + b );
			} else {
				res = (float) ( c/2 * ( Math.pow( t / (d/2) - 2, 5 ) + 2 ) + b );
			}
			break;
		case EASE_IN_SINE:
			res = (float) ( -c * Math.cos( t/d * ( Math.PI / 2 ) ) + c + b );
			break;
		case EASE_OUT_SINE:
			res = (float) ( c * Math.sin( t/d * ( Math.PI / 2 ) ) + b );
			break;
		case EASE_IN_OUT_SINE:
			res = (float) ( -c/2 * ( Math.cos( t/d * Math.PI ) - 1 ) + b );
			break;
		case EASE_IN_EXPO:
			res = ( t == 0 ) ? b : (float) ( c * Math.pow( 2, 10 * ( t/d - 1 ) ) + b );
			break;
		case EASE_OUT_EXPO:
			res = ( t == d ) ? b + c : (float) ( c * ( -Math.pow( 2, -10 * t/d ) + 1 ) + b );
			break;
		case EASE_IN_OUT_EXPO:
			if ( t == 0 ) {
				res = b;
			} else if ( t == d ) {
				res = b + c;
			} else if ( t < d/2 ) {
				res = (float) ( c/2 * Math.pow( 2, 10 * (t / (d/2) - 1) ) + b );
			} else {
				res = (float) ( c/2 * ( -Math.pow( 2, -10 * ( t / (d/2) - 1 ) ) + 2 ) + b );
			}
			break;
		case EASE_IN_CIRC:
			res = (float) ( -c * (Math.sqrt(1 - Math.pow( t/d, 2 ) ) - 1 ) + b );
			break;
		case EASE_OUT_CIRC:
			res = (float) ( c * Math.sqrt(1 - Math.pow( t/d - 1, 2 ) ) + b );
			break;
		case EASE_IN_OUT_CIRC:
			if ( t < d/2 ) {
				res = (float) ( -c/2 * ( Math.sqrt( 1 - Math.pow( t / (d/2), 2 ) ) - 1 ) + b );
			} else {
				res = (float) ( c/2 * ( Math.sqrt( 1 - Math.pow( t / (d/2) - 2, 2 ) ) + 1 ) + b );
			}
			break;
		case EASE_IN_ELASTIC:
			s = (float) 1.70158;
			p = 0;
			a = c;
			if ( t == 0 ) {
				res = b;
			} else if ( t/d == 1 ) {
				res = b + c;
			} else {
				if ( p == 0 ) p = (float) (d * 0.3);
				if ( a < Math.abs( c ) ) {
					a = c;
					s = p / 4;
				} else {
					s = (float) (p / ( 2 * Math.PI ) * Math.asin( c/a ));
				}
				res = (float) (-( a * Math.pow( 2, 10 * ( t/d - 1 ) ) * Math.sin( ( ( t/d - 1 ) * d - s) * ( 2 * Math.PI ) / p ) ) + b);
			}
			break;
		case EASE_OUT_ELASTIC:
			s = (float) 1.70158;
			p = 0;
			a = c;
			if ( t == 0 ) {
				res = b;
			} else if ( t/d == 1 ) {
				res = b + c;
			} else {
				if ( p == 0 ) p = (float) (d * 0.3);
				if ( a < Math.abs( c ) ) {
					a = c;
					s = p / 4;
				} else {
					s = (float) (p / ( 2 * Math.PI ) * Math.asin( c/a ));
				}
				res = (float) (a * Math.pow( 2, -10 * t/d ) * Math.sin( ( t - s ) * ( 2 * Math.PI ) / p ) + c + b);
			}
			break;
    case EASE_IN_OUT_ELASTIC:
    	s = (float) 1.70158;
    	p = 0;
    	a = c;
    	if ( t == 0 ) {
    		res = b;
    	} else if ( t/d == 1 ) {
    		res = b + c;
    	} else {
    		if ( p == 0 ) p = (float) (d * 0.3);
    		if ( a < Math.abs( c ) ) {
    			a = c;
    			s = p / 4;
    		} else {
    			s = (float) (p / ( 2 * Math.PI ) * Math.asin( c/a ));
    		}
    		if ( t < (d/2) ) {
    			res = (float) (-0.5 * ( a * Math.pow( 2, 10 * ( t/(d/2) - 1 ) ) * Math.sin( ( 2 * t - s ) * ( 2 * Math.PI ) / p ) ) + b);
    		} else {
    			res = (float) (a * Math.pow( 2, -10 * ( t/(d/2) - 1 ) ) * Math.sin( ( 2 * t - s ) * ( 2 * Math.PI ) / p ) * 0.5 + c + b);
    		}
    	}
    	break;
    case EASE_IN_BACK:
    	s = (float) 1.70158;
    	res = (float) (c * Math.pow( t/d, 2 ) * ( ( s + 1 ) * t/d - s ) + b);
    	break;
    case EASE_OUT_BACK:
    	s = (float) 1.70158;
    	res = (float) (c * ( Math.pow( t/d - 1, 2 ) * ( ( s + 1 ) * ( t/d - 1 ) + s ) + 1 ) + b);
    	break;
    case EASE_IN_OUT_BACK:
    	s = (float) 1.70158;
    	if ( t < d/2 ) {
    		s *= 1.525;
    		res = (float) (c / 2 * ( Math.pow( t / (d/2), 2 ) * ( ( s + 1 ) * ( t / (d/2) ) - s ) ) + b);
    	} else {
    		s *= 1.525;
    		res = (float) (c / 2 * ( Math.pow( t / (d/2) - 2, 2 ) * ( ( s + 1 ) * ( t / (d/2) - 2 ) + s ) + 2 ) + b);
    	}
    	break;
    case EASE_IN_BOUNCE:
    	res = c - this.ease( EASE_OUT_BOUNCE, d - t, 0, c, d ) + b;
    	break;
    case EASE_OUT_BOUNCE:
    	if ( t/d < 1/2.75 ) {
    		res = (float) (c * ( 7.5625 * Math.pow( t/d, 2 ) ) + b);
    	} else if ( t/d < 2/2.75 ) {
    		res = (float) (c * ( 7.5625 * Math.pow( t/d - 1.5/2.75, 2 ) + 0.75 ) + b);
    	} else if ( t/d < 2.5/2.75 ) {
    		res = (float) (c * ( 7.5625 * Math.pow( t/d - 2.25/2.75, 2 ) + 0.9375 ) + b);
    	} else {
    		res = (float) (c * ( 7.5625 * Math.pow( t/d - 2.625/2.75, 2 ) + 0.984375 ) + b);
    	}
    	break;
    case EASE_IN_OUT_BOUNCE:
    	if ( t < d/2 ) {
    		res = (float) (this.ease( EASE_IN_BOUNCE, t * 2, 0, c, d ) * 0.5 + b);
    	} else {
    		res = (float) (this.ease( EASE_OUT_BOUNCE,  t * 2 - d, 0, c, d ) * 0.5 + c * 0.5 + b);
    	}
    	break;
    default:
    	res = b + c * t / d;
    	break;
		}
		
		return res;
	}
}