#version 120

varying vec2 texCoord0;

uniform vec4 color;
uniform sampler2D sampler;

void main()
{
    vec4 textureColor = texture2D(sampler, texCoord0.xy);

    if(textureColor == vec4(0,0,0,0))
        gl_FragColor = color;
    else
        gl_FragColor = textureColor * color;
}